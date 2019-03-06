package com.trackster.tracksterapp.main

import com.trackster.tracksterapp.base.BaseContract
import com.trackster.tracksterapp.model.Shipment

interface MainContract {

    //Region VIEW
    interface View : BaseContract.BaseView {

        fun updateLoads(loadsList: ArrayList<Shipment>)


//        fun userRetrieved(user: User)
//
//        fun onVIpUserAutenticated(vipMemberUserInfoResponse: VipMemberUserInfoResponse)
//
//        fun onUserChangesSaved()
//
//        fun updateFutureEventsregistartions(upcomingEvents : ArrayList<Place>)
//
//        fun updatePastEventsRegistartions(pastEvents : ArrayList<Place>)
//
//        fun onNewsFeedLiked()
//
//        fun onLanguageUpdated()
//
//        fun updateEvents(searchResult: SearchResult)
//
//        fun updateNewsfeed(newsfeedList: ArrayList<NewsFeed>)
//
//        fun updateSupportedCountries(supportedCountries: ArrayList<SupportedCountry>)
//
//        fun updateCities(cities: ArrayList<City>)
//
//        fun updatePhotos(userPictures: UserPictures)
//
//        fun updateUserFriends(userFriends: UserFriends)
//
//        fun updatePastEvents(places: ArrayList<Place>)
//
//        fun updatePictureDetails(pictureDetails: PictureDetails)
//
//        fun updatePlaceDetails(place: Place)
//
//        fun updateComments(comments: ArrayList<Comment>)
//
//        fun updatePeopleGoing(peopleGoing: ArrayList<PeopleGoing>)
//
//        fun updateRatings(ratings: ArrayList<Rating>)
//
        fun updateView()
//
//        fun updateWebView(webViewData: WebViewData?)
//
//        fun updateCheckContacts(friends: ArrayList<Friend>)
//
//        fun displayUser(editUserProfileResponse: EditUserProfileResponse)
//
//        fun resetPassword(resetPasswordRequest: ResetPasswordRequest)
//
//        fun onFriendAdded(friendId: String)
//
//        fun onFriendConfirmed()
//
//        fun onFriendInvited()
//
//        fun onUserInvitedToEvent()
//
//        fun onMusicGenresReceived(musicData: MusicPropertiesDataModel)
//
//        fun onEditedUsernameIsUnique(newNickname: String)
//
//        fun onGetMyFriendListCompleted(userFriends: UserFriends)
//
//        fun onSuccessUpcomingEvents(events: ArrayList<Place>)
//
//        fun onBannersReceived(bannerDataResponse: BannerDataResponse)
//
//        fun onPasswordChangeError()
//
//        fun onVipLoginFailed(throwable: Throwable)
//
//        interface Notification : BaseContract.BaseView {
//            fun updateNotificationList(notificationList: List<NotificationListItemResponse>)
//
//            fun updatePlaceDetails(place: Place)
//
//            fun setFriendsList(userFriends: UserFriends, notificationItem: NotificationListItemResponse)
//        }
    }
    //END Region VIEW

    //Region PRESENTER
    interface Presenter : BaseContract.BasePresenter {

//        fun getPastEventsRegistrations()
//
//        fun getFutureEventRegistrations()
//
//        fun getVipMembershipUser()
//
//        fun loginUserToVipMembership(vipMemberCredentials: VipMemberLoginRequest)
//
//        fun getBannerFeed()
//
//        fun getMusicGenres()
//
//        fun updateMusicGenres(musicProperty: MusicPropertiesDataModel)
//
//        fun getClubEvents(placeId: Int)
//
//        fun setUserLangauge(langCode: Int)
//
//        fun getMyFriendList()
//
//        fun inviteContactsToEvent(inviteContacsToEventModel: InviteContacsToEventModel, placeId: String)
//
//        fun getMe()
//
//        fun getNewsfeed(pageNum: Int, pageSize: Int, filter: Int, userId: String, isProgressBarRefresh: Boolean)
//
//        fun getEvents(homeEventFilterRequest: HomeEventFilterRequest, isProgressBarRefresh: Boolean)
//
//        fun getSupportedCountries()
//
//        fun getCities(countryId: String)
//
//        fun getUserTagPictures(userId: String)
//
//        fun getUserFriends(userId: String)
//
//        fun getPastEvents(id: Int)
//
//        fun getPictureDetails(pictureId: Int)
//
//        fun getPlaceDetails(placeId: Int)
//
//        fun comment(commentHomeRequest: CommentRequest)
//
//        fun getComments(id: Int, commentType: Int)
//
//        fun getPeopleGoing(placeId: Int)
//
//        fun getRatings(placeId: Int)
//
//        fun report(reportRequest: ReportRequest)
//
//        fun likeNewsfeed(likeRequest: LikeRequest)
//
//        fun unlikeNewsfeed(likeRequest: LikeRequest)
//
//        fun likePlace(likeRequest: LikeRequest)
//
//        fun unlikePlace(likeRequest: LikeRequest)
//
//        fun postFeedback(postFeedbackRequest: PostFeedbackRequest)
//
//        fun tag(tagRequest: TagRequest)
//
//        fun untag(tagRequest: TagRequest)
//
//        fun likePicture(likeRequest: LikeRequest)
//
//        fun unlikePicture(likeRequest: LikeRequest)
//
//        fun editFeedback(editFeedbackRequest: EditFeedbackRequest)
//
//        fun postGoing(goingRequest: GoingRequest)
//
//        fun postCancelGoing(goingRequest: GoingRequest)
//
//        fun getHtml(webViewRequest: WebViewRequest)
//
//        fun checkContacts(checkContactsRequest: CheckContactsRequest)
//
//        fun addFriend(friendRequest: FriendRequest)
//
//        fun confirmFriend(friendRequest: FriendRequest)
//
//        fun inviteUsersToApplication(friendList: List<Friend>)
//
//        fun postProfilePicture(profilePictureRequest: ProfilePictureRequest)
//
//        fun getUserProfile()
//
//        fun updateUserProfile(EditUserProfileResponse: EditUserProfileResponse)
//
//        fun send(resetPasswordRequest: ResetPasswordRequest)
//
//        fun inviteUserToEvent(inviteUsersToEventModel: InviteUsersToEventModel)
//
//        fun checkEditedNicknameUnique(newNickname: String)
//
//        interface GetMeRequestListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetMe(user: User)
//        }
//
//        interface GetNewsFeed : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetNewsfeed(newsfeedList: ArrayList<NewsFeed>)
//        }
//
//        interface GetEventsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetEvents(searchResult: SearchResult)
//        }
//
//        interface GetSupportedCountriesListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessSupportedCountries(supportedCountries: ArrayList<SupportedCountry>)
//        }
//
//        interface GetCitiesListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessCities(cities: ArrayList<City>)
//        }
//
//        interface GetUserTagPicturesListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessUserTagPictures(userPictures: UserPictures)
//        }
//
//        interface FriendActionsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessUserFriends(userFriends: UserFriends)
//            fun onSuccesAddFriend(friendId: String)
//            fun onSuccessConfirmFriend()
//            fun onFriendInvitation()
//            fun onSuccessGetMyFriendList(userFriends: UserFriends)
//        }
//
//        interface GetClubEventsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetPastEvents(places: ArrayList<Place>)
//            fun onSuccessGetUpcomingEvents(places: ArrayList<Place>)
//
//            fun onSuccessGetpastEventsForUser(events : ArrayList<Place>)
//            fun onSuccessGetFutureEventsForUser(events: ArrayList<Place>)
//        }
//
//        interface GetPictureDetailsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetPictureDetails(pictureDetails: PictureDetails)
//        }
//
//        interface GetPlaceDetailsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetPlaceDetails(place: Place)
//
//            fun onUserInvitedToEventSuccess()
//        }
//
//        interface GetCommentsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetComments(comments: ArrayList<Comment>)
//        }
//
//        interface GetPeopleGoingListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessPeopleGoing(peopleGoing: ArrayList<PeopleGoing>)
//        }
//
//        interface GetRatingsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessRatings(ratings: ArrayList<Rating>)
//        }
//
//        interface DefaultRequestListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessUpdateView()
//            fun onMusicGenresReceived(musicData: MusicPropertiesDataModel)
//            fun onBannersFeedReceived(bannerDataResponse: BannerDataResponse)
//            fun onVipUserDetails(vipMemberUserInfoResponse: VipMemberUserInfoResponse)
//            fun onVipLoginFailed(throwable: Throwable)
//        }
//
//
//        interface GetHtmlRequestListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetHtml(webViewData: WebViewData?)
//        }
//
//        interface CheckContactsListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessCheckContacts(friends: ArrayList<Friend>)
//        }
//
//        interface GetPhotoListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccessGetPhoto(profilePictureRequest: ProfilePictureRequest)
//        }
//
//        interface SendRequestListener : BaseContract.BasePresenter.DataCallback {
//            fun onSuccess(resetPasswordRequest: ResetPasswordRequest)
//
//            fun onPasswordChangeError()
//        }
//
//        interface OnGetEditUserListener : BaseContract.BasePresenter.DataCallback {
//            fun onGetEditUserSuccess(user: EditUserProfileResponse)
//            fun onUsernameIsUniqueSuccess(nickname: String)
//            fun onSuccesUpdatedUserData(user: EditUserProfileResponse)
//            fun onUserLanguageChanged()
//        }
//
//        interface Notifications : BaseContract.BasePresenter {
//
//            fun getNotifications(schouldRefresh: Boolean = true)
//
//            fun getPlaceDetails(placeId: Int)
//
//            fun getFriends(notificationItem: NotificationListItemResponse)
//
//            interface NotificationsListener : BaseContract.BasePresenter.DataCallback {
//
//                fun onSuccessGetNotifications(notificationList: List<NotificationListItemResponse>)
//            }
//
//            interface GetPlaceDetailsListener : BaseContract.BasePresenter.DataCallback {
//                fun onSuccessGetPlaceDetails(place: Place)
//            }
//
//            interface GetFriendsListener : BaseContract.BasePresenter.DataCallback {
//                fun onGetFriendsSuccess(userFriends: UserFriends, notificationItem: NotificationListItemResponse)
//            }
//        }
    }

    //Region INTERACTOR
    interface Interactor : BaseContract.BaseInteractor {

//        fun getBannerFeed()
//
//        fun loginUserVipMember(vipMemberCredentials: VipMemberLoginRequest)
//
//        fun getVipUserDetails()
//
//        fun getMusicGenres()
//
//        fun updateMusicGenres(musicData: MusicPropertiesDataModel)
//
//        fun getClubEvents(placeId: Int)
//
//        fun setUserLangauge(langCode: Int)
//
//        fun inviteContactsToEvent(inviteContacsToEventModel: InviteContacsToEventModel, placeId: String)
//
//        fun getMe()
//
//        fun getNewsfeed(pageNum: Int, pageSize: Int, filter: Int, userId: String)
//
//        fun getEvents(homeEventFilterRequest: HomeEventFilterRequest)
//
//        fun getSupportedCountries()
//
//        fun getCities(countryId: String)
//
//        fun getUserTagPictures(userId: String)
//
//        fun getUserFriends(userId: String)
//
//        fun getPastEvents(id: Int)
//
//        fun getPictureDetails(pictureId: Int)
//
//        fun getPlaceDetails(placeId: Int)
//
//        fun getPeopleGoing(placeId: Int)
//
//        fun getRatings(placeId: Int)
//
//        fun report(reportRequest: ReportRequest)
//
//        fun likeNewsfeed(likeRequest: LikeRequest)
//
//        fun unlikeNewsfeed(likeRequest: LikeRequest)
//
//        fun likePlace(likeRequest: LikeRequest)
//
//        fun unlikePlace(likeRequest: LikeRequest)
//
//        fun postFeedback(postFeedbackRequest: PostFeedbackRequest)
//
//        fun tag(tagRequest: TagRequest)
//
//        fun untag(tagRequest: TagRequest)
//
//        fun likePicture(likeRequest: LikeRequest)
//
//        fun unlikePicture(likeRequest: LikeRequest)
//
//        fun editFeedback(editFeedbackRequest: EditFeedbackRequest)
//
//        fun postGoing(goingRequest: GoingRequest)
//
//        fun postCancelGoing(goingRequest: GoingRequest)
//
//        fun comment(commentHomeRequest: CommentRequest)
//
//        fun getComments(id: Int, commentType: Int)
//
//        fun getHtml(webViewRequest: WebViewRequest)
//
//        fun checkContacts(checkContactsRequest: CheckContactsRequest)
//
//        fun addFriend(friendRequest: FriendRequest)
//
//        fun postProfilePicture(profilePictureRequest: ProfilePictureRequest)
//
//        fun send(resetPasswordRequest: ResetPasswordRequest)
//
//        fun getUserProfile()
//
//        fun checkEditedNicknameUnique(newNickname: String)
//
//        fun updateUserProfile(EditUserProfileResponse: EditUserProfileResponse)
//
//        fun confirmFriend(friendRequest: FriendRequest)
//
//        fun inviteUsersToApplication(friendList: List<Friend>)
//
//        fun inviteUserToEvent(inviteUsersToEventModel: InviteUsersToEventModel)
//
//        fun getMyFriendList()
//
//        fun getNotifications()
//
//        fun getPastEventsRegistrations()
//
//        fun getFutureEventsRegistrations()
        //END INTERACTOR
    }
}