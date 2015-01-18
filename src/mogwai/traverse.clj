(ns mogwai.traverse
  (:refer-clojure :exclude [count])
  (:import
   [com.tinkerpop.gremlin.structure Compare]
   [com.tinkerpop.gremlin.util.function SFunction]))

(def comparators
  {:lt  Compare/LESS_THAN
   :lte Compare/LESS_THAN_EQUAL
   :eq  Compare/EQUAL
   :gte Compare/GREATER_THAN_EQUAL
   :gt  Compare/GREATER_THAN
   :neq Compare/NOT_EQUAL})

(defn str-args [coll]
  (into-array String (map name coll)))

(defn vertices [g]
  (.V g))

(defn in [t & labels]
  (.in t (str-args labels)))

(defn out [t & labels]
  (.out t (str-args labels)))

(defn in-edges [t & labels]
  (.inE t (str-args labels)))

(defn out-edges [t & labels]
  (.outE t (str-args labels)))

(defn in-vertices [t]
  (.inV t))

(defn out-vertices [t]
  (.outV t))

(defn value [t label]
  (.value t (name label)))

(defn values [t & labels]
  (.values t (str-args labels)))

(defn has
  ([t k]
     (.has t (name k)))
  ([t k v]
     (.has t (name k) v))
  ([t k rel v]
     (.has t (name k) (comparators rel) v)))

(defn count [t]
  (.count t))

(defn wrap-unary [f]
  (reify SFunction
    (apply [_ x]
      (f x))))

(defn group-by [t f]
  (.groupBy t (wrap-unary f)))

(defn group-count-by [t f]
  (.groupCount t (wrap-unary f)))

(defn order [t f]
  (.order t f))

(defn range [t lower upper]
  (.range t lower upper))

(defn next!
  "Retrieve the first element from a traverser's result set"
  [t]
  (.next t))

(defn ->traversal
  "Get the current traversal step of a traverser."
  [traverser]
  (.get traverser))
