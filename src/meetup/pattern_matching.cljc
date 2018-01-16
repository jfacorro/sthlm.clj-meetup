(ns meetup.pattern-matching)

#?(:clj
   (defn process-list
     [[x & xs]]
     (when x
       (prn x)
       (recur xs)))
   :clje
   (defn* process-list
  ([#erl()])
  ([#erl(x & xs)]
    (prn x)
    (recur xs))))

(process-list #?(:clj  (range 10)
                 :clje (lists/seq 0 9)))
