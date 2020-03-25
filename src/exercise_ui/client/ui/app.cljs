(ns exercise-ui.client.ui.app
  (:require
    [clojure.string :as string]
    [bloom.commons.pages :refer [path-for]]
    [bloom.commons.fontawesome :as fa]
    [re-frame.core :refer [dispatch subscribe]]
    [reagent.core :as r]
    [exercise-ui.client.pages :as pages]
    [exercise-ui.utils :as utils]))

(defn header-view []
  [:div.header
   [:h1 "{}"]
   [:nav
    [:a.main {:href (path-for :exercises)
              :class (when (= @pages/current-page-id
                              :exercises)
                       "active")}
     "exercises"]
    [:a {:href (path-for :setup)
         :class (when (= @pages/current-page-id
                         :setup)
                  "active")}
     "setup"]
    [:a {:href (path-for :shortcuts)
         :class (when (= @pages/current-page-id
                         :shortcuts)
                  "active")}
     "shortcuts"]
    [:a {:href (path-for :reference-example)
         :class (when (= @pages/current-page-id
                         :reference-example)
                  "active")}
     "code sample"]
    [:a {:href "https://www.clojuredocs.org"
         :target "_blank"
         :rel "noopener noreferrer"}
     "clojuredocs"]
    [:a {:href "https://cognitory.github.io/clojure-cheatsheet/"
         :target "_blank"
         :rel "noopener noreferrer"}
     "clj-cheatsheet"]
    #_[:a {:href (path-for :pastebin)
         :class (when (= @pages/current-page-id
                         :pastebin)
                  "active")}
     "share"]
    [:a {:href "https://help.clojurecraft.com"
         :target "_blank"
         :rel "noopener noreferrer"}
     "help"]]

   [:div.gap]

   [:div.user
    [:button {:on-click (fn [e]
                          (dispatch [:log-out!]))}
     [fa/fa-sign-out-alt-solid]]]])

(defn main-view []
  [:div
   [header-view]
   [pages/current-page-view]])

(defn login-view []
  (let [user-email (r/atom "")
        code (r/atom "")
        error (r/atom nil)
        state (r/atom :log-in)]
    (fn []
      (case @state
        :waiting
        [:div.log-in "Requesting..."]

        :sent
        [:div.log-in
         "Check your email for a log-in link"
         [:button {:on-click (fn [] (reset! state :log-in))}
          "Try Again"]]

        :log-in
        [:div.log-in
         [:h1 "Clojure Workshop"]
         (when @error
           [:div.error "Invalid Email or Code"])
         [:form
          {:on-submit
           (fn [e]
             (.preventDefault e)
             (reset! error nil)
             (when (not (string/blank? @user-email))
               (dispatch [:request-email! @user-email @code
                          (fn [] (reset! state :sent))
                          (fn []
                            (reset! state :log-in)
                            (reset! error true))])
               (reset! state :waiting)))}
          [:label
           [:span "Email:"]
           [:input {:type "email"
                    :placeholder "foo@example.com"
                    :on-change (fn [e]
                                 (->> (.. e -target -value)
                                      (reset! user-email)))
                    :value @user-email}]]
          [:label
           [:span "Access Code:"]
           [:input {:type "text"
                    :placeholder ""
                    :value @code
                    :on-change (fn [e]
                                 (->> (.. e -target -value)
                                      (reset! code)))}]]
          [:button "Log In"]]]))))

(defn app-view []
  [:div
   (cond
     (not @(subscribe [:logged-in?]))
     [login-view]

     @(subscribe [:loading?])
     [:div.loading "Loading..."]

     :else
     [main-view])])
