(ns clojure-adt.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.match :refer [matchm]]
            [clojure-adt.core :refer [data]]))

(data Tree
  (Branch left right)
  (Leaf data))

(deftest test-matching
  (let [miss-atom (atom nil)
        branch (Branch. :the-left :the-right)]
    (matchm branch
      {:type Branch :left x :right y}
      (do
        (is (= x :the-left))
        (is (= y :the-right)))

      {:type Leaf :data d}
      (reset! miss-atom :leaf)

      :else (reset! miss-atom :fallthrough))
    (is (nil? @miss-atom))))
