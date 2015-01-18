(ns mogwai.graph
  (:refer-clojure :exclude [assoc! conj!])
  (:import [com.tinkerpop.gremlin.structure Edge Vertex]))

(defn v
  "Get a vertex given an id"
  [g id]
  (.v g id))

(defn e
  "Get an edge given an id"
  [g id]
  (.e g id))

(defprotocol ToProps
  (to-props [_]))

(extend-protocol ToProps

  clojure.lang.ISeq
  (to-props [v]
    (mapcat
      #(vector (name (first %)) (second %))
      (partition 2 v)))
  
  clojure.lang.Sequential
  (to-props [v]
    (to-props (seq v)))
  
  clojure.lang.IPersistentMap
  (to-props [m]
    (mapcat
      #(vector (name (first %)) (second %))
      (seq m)))
  
  java.util.Map
  (to-props [m]
    (to-props (into {} m))))

(defn- var-args [coll]
  (into-array Object coll))

(defn add-vertex!
  "Primitive form for adding a vertex to a graph"
  [g props]
  (.addVertex g (var-args (to-props props))))

(defn conj!
  "Add a document to the graph and return the graph"
  [g & props]
  (doseq [prop props]
    (.addVertex g (var-args (to-props prop))))
  g)

(defn add-edge!
  "Primitive form for adding an edge to a vertex"
  [v0 rel v1 & kvs]
  (.addEdge v0 (name rel) v1 (var-args kvs)))

(defn entity-id [entity]
  (.id entity))

(defn property
  "Get or set a property of an element"
  ([entity k]
     (.property entity k))
  ([entity k v]
     (.property entity k v))
  ([entity k v & kvs]
     (.property entity k v (var-args kvs))))

(defn assoc!
  ([entity k v]
     (property entity (name k) v))
  ([entity k v & kvs]
     (apply property entity (name k) v (to-props (vec kvs)))))

(defn merge! [entity other]
  (apply property entity (to-props other)))

(defn remove!
  "Remove an element from the graph"
  [entity]
  (.remove entity))
