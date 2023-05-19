(ns io.github.kit.gif2html.test-utils
  (:require
    [io.github.kit.gif2html.core :as core]
    [integrant.repl.state :as state]
    [migratus.core :as migratus]
    [next.jdbc :as jdbc]))

(defn system-state
  []
  (or @core/system state/system))

(defn clear-db-and-rerun-migrations-fixture
  [f]
  (jdbc/execute! (:db.sql/connection (system-state))
                 ["do
$$
    declare
        row record;
    begin
        for row in select * from pg_tables where schemaname = 'public'
            loop
                execute 'DROP TABLE public.' || quote_ident(row.tablename) || ' CASCADE';
            end loop;
    end;
$$;"])
  (migratus/migrate (:db.sql/migrations (system-state)))
  (f))

(defn system-fixture
  []
  (fn [f]
    (when (nil? (system-state))
      (core/start-app {:opts {:profile :test}}))
    (f)))
