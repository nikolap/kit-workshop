(ns io.github.kit.gif2html.web.controllers.gifs
  (:require
    [gif-to-html.convert :as convert]
    [hato.client :as hato]
    [ring.util.http-response :as http-response]))

(def Gif
  [:map
   [:id integer?]
   [:link string?]
   [:name string?]])

(defn save-gif [{:keys [query-fn http-client] :as opts} {{params :body} :parameters}]
  (try
    (->> (hato/get
           (:link params)
           {:http-client http-client
            :as          :stream})
         :body
         (convert/gif->html)
         (assoc params :ascii)
         (query-fn :create-gif!)
         (first)
         (http-response/ok))
    (catch Exception _e
      (http-response/internal-server-error))))

(defn list-gifs [{:keys [query-fn] :as opts} _]
  (http-response/ok (query-fn :list-gifs {})))

(defn get-gif-by-id [{:keys [query-fn] :as opts} {{params :path} :parameters}]
  (http-response/ok (query-fn :get-gif-by-id params)))