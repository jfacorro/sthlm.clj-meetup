#!/usr/bin/env escript
%% -*- erlang -*-
%%! -smp enable -sname

-mode(compile).

-export([main/1]).

main([]) ->
  main(["1000"]);
main([NStr]) ->
  N = erlang:list_to_integer(NStr),
  {Time, ok} = timer:tc(fun loop/2, [N, N]),
  io:format("~w msecs",[erlang:trunc(Time / 1000)]).

loop(0, _) ->
  ok;
loop(N, X) ->
  loop(X),
  loop(N - 1, X).

loop(0) ->
  ok;
loop(N) ->
  loop(N - 1).
