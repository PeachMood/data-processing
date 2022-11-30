(ns task3)

(defn pfilter
  [pred coll]
  (let [chunk-size (int (Math/ceil (Math/sqrt (count coll))))
        parts (partition-all chunk-size coll)]
    (->> parts
         (map #(future (doall (filter pred %))))
         (doall)
         (map deref)
         (apply concat))))