(ns metabase.query-processor.middleware.cache-backend.interface
  "Interface used to define different Query Processor cache backends.
   Defining a backend is straightforward: define a new namespace with the pattern

     metabase.query-processor.middleware.cache-backend.<backend>

   Where backend is a key representing the backend, e.g. `db`, `redis`, or `memcached`.

   In that namespace, create an object that reifies (or otherwise implements) `IQueryProcessorCacheBackend`.
   This object *must* be stored in a var called `instance`.

   That's it. See `metabase.query-processor.middleware.cache-backend.db` for a complete example of how this is done.")

(defprotocol IQueryProcessorCacheBackend
  "Protocol that different Metabase cache backends must implement.
   (QUERY-HASH as passed below is a byte-array representing a 512-byte SHA3 hash; encode this as needed for use as a
   cache entry key.)"
  (cached-results [this, query-hash, ^Integer max-age-seconds]
    "Return cached results for the query with QUERY-HASH if those results are present in the cache and are less
     than MAX-AGE-SECONDS old. Otherwise, return `nil`.")

  (save-results! [this query-hash results]
    "Add a cache entry with the RESULTS of running query with QUERY-HASH.
     This should replace any prior entries for QUERY-HASH and update the cache timestamp to the current system time.
     (This is also an appropriate point to purge any entries older than the value of the `query-caching-max-ttl` Setting.)"))
