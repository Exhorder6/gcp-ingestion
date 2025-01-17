# Link to code removed from this repository

- 2021-03-12 (available until [commit `c05fbab`](https://github.com/mozilla/gcp-ingestion/commit/c05fbabcb44c5a8a290be311a87951728cf587b6))
  - Remove support for Account Ecosystem Telemetry (AET);
    see [Bug 1697602](https://bugzilla.mozilla.org/show_bug.cgi?id=1697602)
  - Also removes `ParseLogEntry` which transforms a Google Cloud Logging
    (`Stackdriver`) `LogEntry` message into one compatible with structured
    ingestion, which could be relevant for future use cases.
- 2020-06-01 (available until [commit `f1d6464`](https://github.com/mozilla/gcp-ingestion/commit/f1d646442b8c1fcd63202ebca91363979b5b2ae2))
  - Remove `DeduplicateByDocumentId` transform, which was intended for use with
    the backfill from `heka` data, but did not perform well and was never used
    in production.
  - Remove `PublishBundleMetrics` which was temporarily useful for investigation
    into potential batch refactoring, but we were able to use that data to determine
    that average bundle size is so small that it would not be reasonable to publish
    individual GCS objects per bundle; see [#501](https://github.com/mozilla/gcp-ingestion/issues/501).
- 2020-02-26 (available until [commit `6551bc7`](https://github.com/mozilla/gcp-ingestion/tree/6551bc737b2b3c9a3d49c6442d8a8bea2e62ef17))
  - Remove support for schema aliases, including the `--schemaAliasesLocation`
    parameter defined in `SinkOptions`.
- 2020-02-04 (available until [commit `7e60dfc`](https://github.com/mozilla/gcp-ingestion/tree/7e60dfcd2dd8f67ca97e44b42468d8550960906f))
  - Remove patched `WritePartition.java` that limits the maximum number of bytes
    in a single BigQuery load job; Beam 2.19 exposes a configuration parameter
    we now use for the same effect.
- 2020-01-24 (available until [commit `16d7702`](https://github.com/mozilla/gcp-ingestion/tree/16d770233c073af07c9b0f7ca6f9a1b4080d71d3))
  - `HekaReader` and the `heka` input type were removed
  - The `sanitizedLandfill` input format was removed along with AWS dependencies
