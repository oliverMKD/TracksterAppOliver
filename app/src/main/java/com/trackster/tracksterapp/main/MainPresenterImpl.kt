package com.trackster.tracksterapp.main

import android.content.Context
import com.trackster.tracksterapp.network.PostApi

class MainPresenterImpl(private val view: MainContract.View,
                        val context: Context,
                        val postApi: PostApi)
    : MainContract.Presenter {


//
//    override fun loginUserToVipMembership(vipMemberCredentials: VipMemberLoginRequest) {
//        view.setLoadingIndicator(true)
//        interactor.loginUserVipMember(vipMemberCredentials)
//    }
//
//
//    override fun getVipMembershipUser() {
//        interactor.getVipUserDetails()
//    }

    private val interactor: MainContract.Interactor = MainInteractorImpl(context,
        postApi)

//    override fun onMusicGenresReceived(musicData: MusicPropertiesDataModel) {
//        view.onMusicGenresReceived(musicData)
//    }
//
//    override fun getBannerFeed() {
//        interactor.getBannerFeed()
//    }
//
//    override fun onBannersFeedReceived(bannerDataResponse: BannerDataResponse) {
//        view.onBannersReceived(bannerDataResponse)
//    }
//
//    override fun getFutureEventRegistrations() {
//        interactor.getFutureEventsRegistrations()
//    }
//
//    override fun getMusicGenres() {
//        interactor.getMusicGenres()
//    }
//
//    override fun updateMusicGenres(musicData: MusicPropertiesDataModel) {
//        interactor.updateMusicGenres(musicData)
//    }
//
//    override fun onSuccessGetUpcomingEvents(places: ArrayList<Place>) {
//        view.onSuccessUpcomingEvents(places)
//    }
//
//    override fun getMe() {
//        view.setLoadingIndicator(true)
//        interactor.getMe()
//    }
//
//
//    override fun getNewsfeed(pageNum: Int, pageSize: Int, filter: Int, userId: String,
//                             isProgressBarRefresh: Boolean) {
//        if (isProgressBarRefresh) {
//            view.setLoadingIndicator(isProgressBarRefresh)
//        }
//
//        interactor.getNewsfeed(pageNum, pageSize, filter, userId)
//    }
//
//    override fun getClubEvents(placeId: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getClubEvents(placeId)
//    }
//
//    override fun getEvents(homeEventFilterRequest: HomeEventFilterRequest, isProgressBarRefresh: Boolean) {
//        if (isProgressBarRefresh) {
//            view.setLoadingIndicator(isProgressBarRefresh)
//        }
//        interactor.getEvents(homeEventFilterRequest)
//    }
//
//    override fun getMyFriendList() {
//        view.setLoadingIndicator(true)
//        interactor.getMyFriendList()
//    }
//
//    override fun onSuccessGetMyFriendList(userFriends: UserFriends) {
//        view.setLoadingIndicator(false)
//        view.onGetMyFriendListCompleted(userFriends)
//    }
//
//    override fun checkEditedNicknameUnique(newNickname: String) {
//        interactor.checkEditedNicknameUnique(newNickname)
//    }
//
//    override fun onUsernameIsUniqueSuccess(nickname: String) {
//        view.onEditedUsernameIsUnique(nickname)
//    }
//
//    override fun getSupportedCountries() {
//        interactor.getSupportedCountries()
//    }
//
//    override fun getCities(countryId: String) {
//        interactor.getCities(countryId)
//    }
//
//
//    override fun setUserLangauge(langCode: Int) {
//        interactor.setUserLangauge(langCode)
//    }
//
//    override fun onUserLanguageChanged() {
//        view.onLanguageUpdated()
//    }
//
//    override fun getUserTagPictures(userId: String) {
//        view.setLoadingIndicator(true)
//        interactor.getUserTagPictures(userId)
//    }
//
//    override fun getUserFriends(userId: String) {
//        view.setLoadingIndicator(true)
//        interactor.getUserFriends(userId)
//    }
//
//    override fun getPastEvents(id: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getPastEvents(id)
//    }
//
//    override fun confirmFriend(friendRequest: FriendRequest) {
//        view.setLoadingIndicator(true)
//        interactor.confirmFriend(friendRequest)
//    }
//
//
//    override fun getPictureDetails(pictureId: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getPictureDetails(pictureId)
//    }
//
//    override fun getPlaceDetails(placeId: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getPlaceDetails(placeId)
//    }
//
//    override fun getPastEventsRegistrations() {
//        interactor.getPastEventsRegistrations()
//    }
//
//    override fun onSuccessGetpastEventsForUser(events: ArrayList<Place>) {
//        view.setLoadingIndicator(false)
//        view.updatePastEventsRegistartions(events)
//    }
//
//    override fun onSuccessGetFutureEventsForUser(events: ArrayList<Place>) {
//        view.setLoadingIndicator(false)
//        view.updateFutureEventsregistartions(events)
//    }
//
//    override fun getPeopleGoing(placeId: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getPeopleGoing(placeId)
//    }
//
//    override fun getRatings(placeId: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getRatings(placeId)
//    }
//
//    override fun report(reportRequest: ReportRequest) {
//        interactor.report(reportRequest)
//    }
//
//    override fun likeNewsfeed(likeRequest: LikeRequest) {
//        interactor.likeNewsfeed(likeRequest)
//    }
//
//    override fun unlikeNewsfeed(likeRequest: LikeRequest) {
//        interactor.unlikeNewsfeed(likeRequest)
//    }
//
//    override fun likePlace(likeRequest: LikeRequest) {
//        interactor.likePlace(likeRequest)
//    }
//
//    override fun unlikePlace(likeRequest: LikeRequest) {
//        interactor.unlikePlace(likeRequest)
//    }
//
//    override fun postFeedback(postFeedbackRequest: PostFeedbackRequest) {
//        view.setLoadingIndicator(true)
//        interactor.postFeedback(postFeedbackRequest)
//    }
//
//    override fun tag(tagRequest: TagRequest) {
//        view.setLoadingIndicator(true)
//        interactor.tag(tagRequest)
//    }
//
//    override fun untag(tagRequest: TagRequest) {
//        view.setLoadingIndicator(true)
//        interactor.untag(tagRequest)
//    }
//
//    override fun likePicture(likeRequest: LikeRequest) {
//        view.setLoadingIndicator(true)
//        interactor.likePicture(likeRequest)
//    }
//
//    override fun unlikePicture(likeRequest: LikeRequest) {
//        view.setLoadingIndicator(true)
//        interactor.unlikePicture(likeRequest)
//    }
//
//    override fun editFeedback(editFeedbackRequest: EditFeedbackRequest) {
//        view.setLoadingIndicator(true)
//        interactor.editFeedback(editFeedbackRequest)
//    }
//
//    override fun postGoing(goingRequest: GoingRequest) {
//        view.setLoadingIndicator(true)
//        interactor.postGoing(goingRequest)
//    }
//
//    override fun postCancelGoing(goingRequest: GoingRequest) {
//        view.setLoadingIndicator(true)
//        interactor.postCancelGoing(goingRequest)
//    }
//
//    override fun comment(commentHomeRequest: CommentRequest) {
//        view.setLoadingIndicator(true)
//        interactor.comment(commentHomeRequest)
//    }
//
//    override fun getComments(id: Int, commentType: Int) {
//        view.setLoadingIndicator(true)
//        interactor.getComments(id, commentType)
//    }
//
//    override fun getHtml(webViewRequest: WebViewRequest) {
//        view.setLoadingIndicator(true)
//        interactor.getHtml(webViewRequest)
//    }
//
//    override fun checkContacts(checkContactsRequest: CheckContactsRequest) {
//        view.setLoadingIndicator(true)
//        interactor.checkContacts(checkContactsRequest)
//    }
//
//    override fun addFriend(friendRequest: FriendRequest) {
//        view.setLoadingIndicator(true)
//        interactor.addFriend(friendRequest)
//    }
//
//    override fun onSuccessGetMe(user: User) {
//        view.setLoadingIndicator(false)
//        view.userRetrieved(user)
//    }
//
//    override fun onVipUserDetails(vipMemberUserInfoResponse: VipMemberUserInfoResponse) {
//        view.setLoadingIndicator(false)
//        PreferenceUtils.saveVipmembershipDetails(context, vipMemberUserInfoResponse)
//        view.onVIpUserAutenticated(vipMemberUserInfoResponse)
//    }
//
//    override fun onVipLoginFailed(throwable: Throwable) {
//        view.setLoadingIndicator(false)
//        view.onVipLoginFailed(throwable)
//    }
//
//    override fun onSuccessGetNewsfeed(newsfeedList: ArrayList<NewsFeed>) {
//        view.setLoadingIndicator(false)
//        view.updateNewsfeed(newsfeedList)
//    }
//
//    override fun onSuccessGetEvents(searchResult: SearchResult) {
//        view.setLoadingIndicator(false)
//        view.updateEvents(searchResult)
//    }
//
//    override fun onSuccessSupportedCountries(supportedCountries: ArrayList<SupportedCountry>) {
//        view.setLoadingIndicator(false)
//        view.updateSupportedCountries(supportedCountries)
//    }
//
//    override fun onSuccessCities(cities: ArrayList<City>) {
//        view.setLoadingIndicator(false)
//        view.updateCities(cities)
//    }
//
//    override fun onSuccessUserTagPictures(userPictures: UserPictures) {
//        view.setLoadingIndicator(false)
//        view.updatePhotos(userPictures)
//    }
//
//    override fun onSuccessUserFriends(userFriends: UserFriends) {
//        view.setLoadingIndicator(false)
//        view.updateUserFriends(userFriends)
//    }
//
//    override fun onSuccessGetPastEvents(places: ArrayList<Place>) {
//        view.setLoadingIndicator(false)
//        view.updatePastEvents(places)
//    }
//
//    override fun onSuccessGetPictureDetails(pictureDetails: PictureDetails) {
//        view.setLoadingIndicator(false)
//        view.updatePictureDetails(pictureDetails)
//    }
//
//    override fun onSuccessGetPlaceDetails(place: Place) {
//        view.setLoadingIndicator(false)
//        view.updatePlaceDetails(place)
//    }
//
//    override fun onSuccessGetComments(comments: ArrayList<Comment>) {
//        view.setLoadingIndicator(false)
//        view.updateComments(comments)
//    }
//
//    override fun onSuccessPeopleGoing(peopleGoing: ArrayList<PeopleGoing>) {
//        view.setLoadingIndicator(false)
//        view.updatePeopleGoing(peopleGoing)
//    }
//
//    override fun onSuccessRatings(ratings: ArrayList<Rating>) {
//        view.setLoadingIndicator(false)
//        view.updateRatings(ratings)
//    }
//
//    override fun onSuccessUpdateView() {
//        view.setLoadingIndicator(false)
//        view.updateView()
//    }
//
//    override fun onSuccessGetHtml(webViewData: WebViewData?) {
//        view.setLoadingIndicator(false)
//        view.updateWebView(webViewData)
//    }
//
//    override fun onSuccessCheckContacts(friends: ArrayList<Friend>) {
//        view.setLoadingIndicator(false)
//        view.updateCheckContacts(friends)
//    }
//
//    override fun postProfilePicture(profilePictureRequest: ProfilePictureRequest) {
//        view.setLoadingIndicator(true)
//        interactor.postProfilePicture(profilePictureRequest)
//    }
//
//    override fun onSuccessGetPhoto(profilePictureRequest: ProfilePictureRequest) {
//        view.setLoadingIndicator(false)
//        view.updateView()
//    }
//
//    override fun onSuccesAddFriend(friendId: String) {
//        view.setLoadingIndicator(false)
//        view.onFriendAdded(friendId)
//    }
//
//    override fun onSuccessConfirmFriend() {
//        view.setLoadingIndicator(false)
//        view.onFriendConfirmed()
//    }
//
//    override fun inviteUsersToApplication(friendList: List<Friend>) {
//        interactor.inviteUsersToApplication(friendList)
//    }
//
//    override fun onFriendInvitation() {
//        view.onFriendInvited()
//    }
//
//    override fun inviteUserToEvent(inviteUsersToEventModel: InviteUsersToEventModel) {
//        if (inviteUsersToEventModel.list.isNotEmpty()) {
//            interactor.inviteUserToEvent(inviteUsersToEventModel)
//        }
//    }
//
//    override fun inviteContactsToEvent(inviteContacsToEventModel: InviteContacsToEventModel, placeId: String) {
//        if (inviteContacsToEventModel.listContacts.isNotEmpty()) {
//            interactor.inviteContactsToEvent(inviteContacsToEventModel, placeId)
//        }
//    }
//
//    override fun onSuccesUpdatedUserData(user: EditUserProfileResponse) {
//        val nickname = if (user.hideMyRealName) user.nickName else "${user.firstName} ${user.lastName}"
//        PreferenceUtils.updateNickname(context, nickname)
//        view.onUserChangesSaved()
//        RxBus.publish(NameChangedEvent(nickname))
//    }
//
//    override fun onUserInvitedToEventSuccess() {
//        view.onUserInvitedToEvent()
//    }
//
//    override fun onFailure(throwable: Throwable) {
//        view.setLoadingIndicator(false)
//        view.handleApiError(throwable)
//    }
//
//    override fun getUserProfile() {
//        interactor.getUserProfile()
//    }
//
//    override fun updateUserProfile(EditUserProfileResponse: EditUserProfileResponse) {
//        interactor.updateUserProfile(EditUserProfileResponse)
//    }
//
//    override fun send(resetPasswordRequest: ResetPasswordRequest) {
//        view.setLoadingIndicator(true)
//        interactor.send(resetPasswordRequest)
//    }
//
//    override fun onPasswordChangeError() {
//        view.onPasswordChangeError()
//    }
//
//    override fun onSuccess(resetPasswordRequest: ResetPasswordRequest) {
//        view.resetPassword(resetPasswordRequest)
//        view.showSuccessMessage(context.getString(R.string.paas_change_success))
//    }
//
//    override fun onGetEditUserSuccess(user: EditUserProfileResponse) {
//        view.displayUser(user)
//    }

    override fun unsubscribe() {
        interactor.unsubscribe()
    }

}