/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.mozilla.telemetry.decoder;

import com.mozilla.telemetry.transforms.MapElementsWithErrors;
import java.util.HashMap;
import java.util.Map;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;

public class ParseUri extends MapElementsWithErrors.ToPubsubMessageFrom<PubsubMessage> {
  private static class InvalidUriException extends Exception { }

  private static class NullUriException extends InvalidUriException { }

  private static final String TELEMETRY_URI_PREFIX = "/submit/telemetry/";
  private static final String[] TELEMETRY_URI_SUFFIX_ELEMENTS = new String[]{
      "document_id", "document_type", "app_name", "app_version", "app_update_channel",
      "app_build_id"};
  private static final String GENERIC_URI_PREFIX = "/submit/";
  private static final String[] GENERIC_URI_SUFFIX_ELEMENTS = new String[]{
      "document_namespace", "document_type", "document_version", "document_id"};

  private static Map<String, String> zip(String[] keys, String[] values) {
    Map<String, String> map = new HashMap<>();
    int length = Math.min(keys.length, values.length);
    for (int i = 0; i < length; i++) {
      map.put(keys[i], values[i]);
    }
    return map;
  }

  @Override
  protected PubsubMessage processElement(PubsubMessage element) throws InvalidUriException {
    // Copy attributes
    final Map<String, String> attributes = new HashMap<>(element.getAttributeMap());

    // parse uri based on prefix
    final String uri = attributes.get("uri");
    if (uri == null) {
      throw new NullUriException();
    } else if (uri.startsWith(TELEMETRY_URI_PREFIX)) {
      // TODO acquire document_version from attributes.get("args")
      attributes.put("document_namespace", "telemetry");
      attributes.putAll(zip(
          TELEMETRY_URI_SUFFIX_ELEMENTS,
          uri.substring(TELEMETRY_URI_PREFIX.length()).split("/")));
    } else if (uri.startsWith(GENERIC_URI_PREFIX)) {
      attributes.putAll(zip(
          GENERIC_URI_SUFFIX_ELEMENTS,
          uri.substring(GENERIC_URI_PREFIX.length()).split("/")));
    } else {
      throw new InvalidUriException();
    }
    attributes.remove("uri");
    return new PubsubMessage(element.getPayload(), attributes);
  }
}