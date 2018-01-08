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

(benchmark [n 1000000] (dotimes [i n]) 10)
