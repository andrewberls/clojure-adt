# clojure-adt

Experimental algebraic data types for Clojure.

## Usage

This project provides the `data` macro, which is used for constructing sum types in a manner similar to [Haskell](https://wiki.haskell.org/Type#Data_declarations).

```clj
(ns example
  (:require [clojure-adt.core :refer [data]]))

(data Tree
  (Branch left right)
  (Leaf data))
```

This simply generates classes for the `Branch` and `Leaf` variants (using `deftype`), with the same field names.
Most importantly, it automatically provides an implementation of `IMatchLookup` so that they can be used with [core.match](https://github.com/clojure/core.match).

Matching is done via map patterns using the field names as keys (core.match does not currently natively handle matching deftypes/defrecords). To handle overlapping field names, you will also need to include the `:type` key with the value of the variant class being matched.

For example (note that you will need to use `matchm` instead of just `match`):

```clj
(let [branch (Branch. (Leaf. 42) (Leaf. 56))]
  (matchm branch
    {:type Branch :left x :right y}
    (println "Matched Branch x:" (pr-str x) "y:" (pr-str y))

    {:type Leaf :data d}
    (println "Matched Leaf d:" (pr-str d))

    :else (println "No match")))

; Matched Branch x: #object[Leaf 42] y: #object[Leaf 56]
```

You can of course extend custom behavior for the generated classes:

```clj
(extend-type Branch
  MyCustomProtocol
  (do-thing [this] ...))

(extend-type Leaf
  MyCustomProtocol
  (do-thing [this] ...))
```
