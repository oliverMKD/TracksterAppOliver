package com.trackster.tracksterapp.main

import android.content.Context
import com.trackster.tracksterapp.network.PostApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainInteractorImpl(private val context: Context,
                         private val postApi: PostApi) : MainContract.Interactor {

    private val disposable: CompositeDisposable = CompositeDisposable()

//    override fun getBannerFeed() {
//        disposable.add(clubCityApi.getBannersAds(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onBannersFeedReceived(it)
//            }, {
//
//            }))
//    }
//
//
//    override fun getMusicGenres() {
//        disposable.add(clubCityApi.getMusicGenres(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onMusicGenresReceived(it)
//            }, {
//                getMeListener.onFailure(it)
//            }))
//    }
//
//    override fun updateMusicGenres(musicData: MusicPropertiesDataModel) {
//        disposable.add(clubCityApi.updateMusicGenres(PreferenceUtils.getAuthorizationToken(context), musicData.getPostModel())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//
//            }, {
//                getMeListener.onFailure(it)
//            }))
//    }
//
//
//    override fun getMe() {
//        disposable.add(clubCityApi.getMe(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                PreferenceUtils.saveUser(context, it)
//                getMeListener.onSuccessGetMe(it)
//            }, {
//                getMeListener.onFailure(it)
//            }))
//    }
//
//
//    override fun getVipUserDetails() {
//        disposable.add(clubCityApi.getVipMemberUserDetails(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onVipUserDetails(it)
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun getNotifications() {
//
//    }
//
//    override fun getFutureEventsRegistrations() {
//        disposable.add(clubCityApi.getFutureEventsRegistrations(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                clubEventsListener.onSuccessGetFutureEventsForUser(it)
//            }, {
//                clubEventsListener.onFailure(it)
//            }))
//    }
//
//    override fun getPastEventsRegistrations() {
//        disposable.add(clubCityApi.getPastEventsRegistartions(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                clubEventsListener.onSuccessGetpastEventsForUser(it)
//            }, {
//                clubEventsListener.onFailure(it)
//            }))
//    }
//
//    override fun loginUserVipMember(vipMemberCredentials: VipMemberLoginRequest) {
//        disposable.add(clubCityApi.loginVipMembership(PreferenceUtils.getAuthorizationToken(context),
//            username = vipMemberCredentials.username,
//            password = vipMemberCredentials.password
//        )
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onVipUserDetails(it)
//            }, {
//                defaultRequestListener.onVipLoginFailed(it)
//            }))
//    }
//
//    override fun getNewsfeed(pageNum: Int, pageSize: Int, filter: Int, userId: String) {
//        disposable.add(clubCityApi.getNewsfeed(PreferenceUtils.getAuthorizationToken(context),
//            pageNum, pageSize, filter, userId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getNewsfeedListener.onSuccessGetNewsfeed(it)
//            }, {
//                getNewsfeedListener.onFailure(it)
//            }))
//    }
//
//
//    override fun getEvents(homeEventFilterRequest: HomeEventFilterRequest) {
//        disposable.add(clubCityApi.search(PreferenceUtils.getAuthorizationToken(context),
//            homeEventFilterRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                searchListener.onSuccessGetEvents(it)
//            }, {
//                searchListener.onFailure(it)
//            }))
//    }
//
//    override fun getClubEvents(placeId: Int) {
//        disposable.add(clubCityApi.getClubEvents(PreferenceUtils.getAuthorizationToken(context), placeId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                clubEventsListener.onSuccessGetUpcomingEvents(it)
//            }, {
//                clubEventsListener.onFailure(it)
//            }))
//    }
//
//    override fun setUserLangauge(langCode: Int) {
//        disposable.add(clubCityApi.setNewLanguage(PreferenceUtils.getAuthorizationToken(context), UserLanguageRequest(langCode))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { getEditProfileListener.onUserLanguageChanged() },
//                { getEditProfileListener.onFailure(it) }
//            ))
//    }
//
//    override fun getSupportedCountries() {
//        disposable.add(clubCityApi.getSupportedCountries()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getSupportedCountriesListener.onSuccessSupportedCountries(it)
//            }, {
//                getSupportedCountriesListener.onFailure(it)
//            }))
//    }
//
//    override fun getCities(countryId: String) {
//        disposable.add(clubCityApi.getCities(countryId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getCitiesListener.onSuccessCities(it)
//            }, {
//                getCitiesListener.onFailure(it)
//            }))
//    }
//
//    override fun getMyFriendList() {
//        disposable.add(clubCityApi.getLoggedUserFriendsList(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                friendActionsListener.onSuccessGetMyFriendList(it)
//            }, {
//                friendActionsListener.onFailure(it)
//            }))
//    }
//
//    override fun getUserTagPictures(userId: String) {
//        disposable.add(clubCityApi.getUserTagPictures(PreferenceUtils.getAuthorizationToken(context),
//            userId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getUserTagPicturesListener.onSuccessUserTagPictures(it)
//            }, {
//                getUserTagPicturesListener.onFailure(it)
//            }))
//    }
//
//    override fun getUserFriends(userId: String) {
//        disposable.add(clubCityApi.getUserFriends(PreferenceUtils.getAuthorizationToken(context),
//            userId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                friendActionsListener.onSuccessUserFriends(it)
//            }, {
//                friendActionsListener.onFailure(it)
//            }))
//    }
//
//
//    override fun checkEditedNicknameUnique(newNickname: String) {
//        disposable.add(clubCityApi.isNicknameUnique
//            (PreferenceUtils.getAuthorizationToken(context), newNickname)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                getEditProfileListener.onUsernameIsUniqueSuccess(newNickname)
//            }, { er ->
//                getEditProfileListener.onFailure(er)
//            }))
//    }
//
//    override fun getPastEvents(id: Int) {
//        disposable.add(clubCityApi.getPastEvents(PreferenceUtils.getAuthorizationToken(context), id)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                clubEventsListener.onSuccessGetPastEvents(it)
//            }, {
//                clubEventsListener.onFailure(it)
//            }))
//    }
//
//    override fun getPictureDetails(pictureId: Int) {
//        disposable.add(clubCityApi.getPictureDetails(PreferenceUtils.getAuthorizationToken(context), pictureId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getPictureDetailsListener.onSuccessGetPictureDetails(it)
//            }, {
//                getPictureDetailsListener.onFailure(it)
//            }))
//    }
//
//    override fun getPlaceDetails(placeId: Int) {
//        disposable.add(clubCityApi.getPlaceDetails(PreferenceUtils.getAuthorizationToken(context), placeId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getPlaceDetailsListener.onSuccessGetPlaceDetails(it)
//            }, {
//                getPlaceDetailsListener.onFailure(it)
//            }))
//    }
//
//    override fun getPeopleGoing(placeId: Int) {
//        disposable.add(clubCityApi.getPeopleGoing(PreferenceUtils.getAuthorizationToken(context), placeId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getPeopleGoingListener.onSuccessPeopleGoing(it)
//            }, {
//                getPeopleGoingListener.onFailure(it)
//            }))
//    }
//
//    override fun getRatings(placeId: Int) {
//        disposable.add(clubCityApi.getRatings(PreferenceUtils.getAuthorizationToken(context), placeId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getRatingsListener.onSuccessRatings(it)
//            }, {
//                getRatingsListener.onFailure(it)
//            }))
//    }
//
//    override fun report(reportRequest: ReportRequest) {
//        disposable.add(clubCityApi.report(PreferenceUtils.getAuthorizationToken(context), reportRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun likeNewsfeed(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.likeNewsfeed(PreferenceUtils.getAuthorizationToken(context), likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun unlikeNewsfeed(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.unlikeNewsfeed(PreferenceUtils.getAuthorizationToken(context), likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun likePlace(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.likePlace(PreferenceUtils.getAuthorizationToken(context), likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun unlikePlace(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.unlikePlace(PreferenceUtils.getAuthorizationToken(context), likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun postFeedback(postFeedbackRequest: PostFeedbackRequest) {
//        disposable.add(clubCityApi.postFeedback(PreferenceUtils.getAuthorizationToken(context), postFeedbackRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun tag(tagRequest: TagRequest) {
//        disposable.add(clubCityApi.tag(PreferenceUtils.getAuthorizationToken(context), tagRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun untag(tagRequest: TagRequest) {
//        disposable.add(clubCityApi.untag(PreferenceUtils.getAuthorizationToken(context), tagRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun likePicture(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.pictureLike(PreferenceUtils.getAuthorizationToken(context),
//            likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun unlikePicture(likeRequest: LikeRequest) {
//        disposable.add(clubCityApi.pictureUnlike(PreferenceUtils.getAuthorizationToken(context),
//            likeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun editFeedback(editFeedbackRequest: EditFeedbackRequest) {
//        disposable.add(clubCityApi.editFeedback(PreferenceUtils.getAuthorizationToken(context),
//            editFeedbackRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun postGoing(goingRequest: GoingRequest) {
//        disposable.add(clubCityApi.postGoing(PreferenceUtils.getAuthorizationToken(context),
//            goingRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun postCancelGoing(goingRequest: GoingRequest) {
//        disposable.add(clubCityApi.postCancelGoing(PreferenceUtils.getAuthorizationToken(context),
//            goingRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun comment(commentHomeRequest: CommentRequest) {
//        disposable.add(clubCityApi.comment(PreferenceUtils.getAuthorizationToken(context),
//            commentHomeRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                defaultRequestListener.onSuccessUpdateView()
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun getComments(id: Int, commentType: Int) {
//        disposable.add(clubCityApi.getComments(PreferenceUtils.getAuthorizationToken(context),
//            id, commentType)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getCommentsListener.onSuccessGetComments(it)
//            }, {
//                getCommentsListener.onFailure(it)
//            }))
//    }
//
//    override fun getHtml(webViewRequest: WebViewRequest) {
//        disposable.add(clubCityApi.getHtml(webViewRequest.langId, webViewRequest.templateId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getHtmlRequestListener.onSuccessGetHtml(it)
//            }, {
//                getHtmlRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun checkContacts(checkContactsRequest: CheckContactsRequest) {
//        disposable.add(clubCityApi.checkContacts(PreferenceUtils.getAuthorizationToken(context), checkContactsRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                checkContactsListener.onSuccessCheckContacts(it)
//            }, {
//                checkContactsListener.onFailure(it)
//            }))
//    }
//
//    override fun addFriend(friendRequest: FriendRequest) {
//        disposable.add(clubCityApi.addFriend(PreferenceUtils.getAuthorizationToken(context), friendRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                friendActionsListener.onSuccesAddFriend(friendRequest.userId)
//            }, {
//                friendActionsListener.onFailure(it)
//            }))
//    }
//
//    override fun confirmFriend(friendRequest: FriendRequest) {
//        disposable.add(clubCityApi.confirmFriend(PreferenceUtils.getAuthorizationToken(context), friendRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                friendActionsListener.onSuccessConfirmFriend()
//            }, {
//                friendActionsListener.onFailure(it)
//            }))
//    }
//
//    override fun postProfilePicture(profilePictureRequest: ProfilePictureRequest) {
//        disposable.add(clubCityApi.postProfilePicture(PreferenceUtils.getAuthorizationToken(context), profilePictureRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                PreferenceUtils.saveUserProfilePicture(context, profilePictureRequest)
//                getPhotoListener.onSuccessGetPhoto(profilePictureRequest)
//            }, {
//                defaultRequestListener.onFailure(it)
//            }))
//    }
//
//    override fun send(resetPasswordRequest: ResetPasswordRequest) {
//        disposable.add(clubCityApi.resetPassword(PreferenceUtils.getAuthorizationToken(context), resetPasswordRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                listener.onSuccess(resetPasswordRequest)
//            }, {
//                listener.onPasswordChangeError()
//            }))
//    }
//
//    override fun getUserProfile() {
//        disposable.add(clubCityApi.getEditUserProfile(PreferenceUtils.getAuthorizationToken(context))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getEditProfileListener.onGetEditUserSuccess(it)
//            }, {
//                getEditProfileListener.onFailure(it)
//            }))
//    }
//
//    override fun updateUserProfile(editUserProfileResponse: EditUserProfileResponse) {
//        disposable.add(clubCityApi.updateEditProfile(PreferenceUtils.getAuthorizationToken(context), editUserProfileResponse)
//            .observeOn(Schedulers.io())
//            .subscribeOn(Schedulers.io()).subscribe({
//                getEditProfileListener.onSuccesUpdatedUserData(editUserProfileResponse)
//            }, {}))
//    }
//
//    override fun inviteUsersToApplication(friendList: List<Friend>) {
//        val userInviteModel = UserInviteModel(arrayListOf<InviteUserToAppRequest>())
//        friendList.forEach { userInviteModel.listUsers.add(InviteUserToAppRequest(it.phoneNumber)) }
//
//        disposable.add(clubCityApi.inviteUserToApp(PreferenceUtils.getAuthorizationToken(context), userInviteModel)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                friendActionsListener.onFriendInvitation()
//            }, {
//                friendActionsListener.onFailure(it)
//            })
//        )
//    }
//
//    override fun inviteUserToEvent(inviteUsersToEventModel: InviteUsersToEventModel) {
//
//        disposable.add(clubCityApi.inviteUserToEvent(PreferenceUtils.getAuthorizationToken(context), inviteUsersToEventModel)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getPlaceDetailsListener.onUserInvitedToEventSuccess()
//            }, {
//                getPlaceDetailsListener.onFailure(it)
//            }))
//
//    }
//
//    override fun inviteContactsToEvent(inviteContacsToEventModel: InviteContacsToEventModel, placeId: String) {
//
//        disposable.add(clubCityApi.inviteContactToEvent(PreferenceUtils.getAuthorizationToken(context), inviteContacsToEventModel, placeId)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                getPlaceDetailsListener.onUserInvitedToEventSuccess()
//            }, {
//                getPlaceDetailsListener.onFailure(it)
//            }))
//    }

    override fun unsubscribe() {
        disposable.dispose()
    }

}