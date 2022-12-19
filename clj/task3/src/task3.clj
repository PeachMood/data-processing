(ns task3)

(defn pfilter
  ([pred coll]
   (let [n (. (Runtime/getRuntime) availableProcessors)
         chunk-size 20
         parts (map doall (partition-all chunk-size coll))
         ffilter #(future (doall (filter pred %)))
         pool (map ffilter parts)
         step (fn step [vs fs]
                (if-let [s (seq fs)]
                  (lazy-seq (lazy-cat (deref (first vs)) (step (rest vs) (rest s))))
                  (apply concat (map deref vs))))]
     (step pool (drop n pool)))))
