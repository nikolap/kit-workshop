(ns io.github.kit.gif2html.web.controllers.gifs
  (:require
   [ring.util.http-response :as http-response]))

(def Gif
  [:map
   [:id integer?]
   [:link string?]
   [:name string?]])

(defn save-gif [{:keys [query-fn] :as opts} {{params :body} :parameters}]
  (query-fn :create-gif! params)
  (http-response/ok {:result :ok}))

(defn list-gifs [{:keys [query-fn] :as opts} _]
  (http-response/ok (query-fn :list-gifs {})))

(defn get-gif-by-id [{:keys [query-fn] :as opts} {{params :path} :parameters}]
  (http-response/ok (query-fn :get-gif-by-id params)))