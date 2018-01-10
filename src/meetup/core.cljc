(ns meetup.core)

(defmacro benchmark
  "Runs expr iterations times in the context of a let expression with
  the given bindings, then prints out the bindings and the expr
  followed by number of iterations and total time. The optional
  argument print-fn, defaulting to println, sets function used to
  print the result. expr's string representation will be produced
  using pr-str in any case."
  {:added "1.0"}
  [bindings expr iterations & {:keys [print-fn] :or {print-fn 'println}}]
  (let [bs-str   (pr-str bindings)
        expr-str (pr-str expr)]
    `(let ~bindings
       (let [start#   #?(:clj (. System (nanoTime))
                         :clje (erlang/monotonic_time :nano_seconds))
             ret#     (dotimes [_# ~iterations] ~expr)
             end#     #?(:clj (. System (nanoTime))
                         :clje (erlang/monotonic_time :nano_seconds))
             elapsed# (int (/ (- end# start#) 1000000))]
         (~print-fn (str ~bs-str ", " ~expr-str ", "
                      ~iterations " runs, " elapsed# " msecs"))))))

(benchmark [n 1000] (dotimes [_ n]) 1000)

#?(:clje
   (do
     (defn* loop-1
       ([0])
       ([n] (loop-1 (erlang/- n 1))))

     (benchmark [n 1000] (loop-1 n) 1000)

     (defn* loop-2
       ([0])
       ([n] (loop-2 (- n 1))))

     (benchmark [n 1000] (loop-2 n) 1000)

     (defn* loop-3
       ([0])
       ([n] (recur (- n 1))))

     (benchmark [n 1000] (loop-3 n) 1000)

     (defn* loop-4
       ([0])
       ([n] (recur (dec n))))

     (benchmark [n 1000] (loop-4 n) 1000)

     (defn* loop-5
       ([0])
       ([n] (recur (erlang/- n 1))))

     (benchmark [n 1000] (loop-5 n) 1000)))
