(ns clojure-adt.core
  (:require [clojure.core.match.protocols]))

(defmacro data
  "Declare a sum type with a set of constructors

   (data Tree
     (Branch left right)
     (Leaf data))

   Automatically generates classes for each variant using `deftype` with the same field names, as
   well as providing an implementation of `IMatchLookup` for use with core.match."
  [ty & variants]
  (let [defns
        (for [variant variants]
          (let [[ctor & fields] variant
                field-kvs (mapcat (fn [f] [(keyword (name f)) f]) fields)]
            `(deftype ~ctor [~@fields]
               clojure.core.match.protocols/IMatchLookup
               (clojure.core.match.protocols/val-at [this# k# not-found#]
                 (case k#
                   ~@field-kvs
                   :type (type this#)
                   not-found#)))))]
    `(do ~@defns)))
