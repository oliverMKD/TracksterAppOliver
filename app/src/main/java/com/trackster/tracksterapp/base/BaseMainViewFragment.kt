package com.trackster.tracksterapp.base

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.ProgressBar
import com.trackster.tracksterapp.main.DaggerMainComponent
import com.trackster.tracksterapp.main.MainActivityContextModule
import com.trackster.tracksterapp.main.MainContract
import com.trackster.tracksterapp.main.MainPresenterModule
import com.trackster.tracksterapp.model.Shipment
import com.trackster.tracksterapp.utils.Utils
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseMainViewFragment : BaseFragment(), MainContract.View {
    override fun onBackStackChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLoadingIndicator(active: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSuccessMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateLoads(loadsList: ArrayList<Shipment>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleApiError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    var compositeDisposableContainer = CompositeDisposable()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val mainComponent = DaggerMainComponent.builder()
            .appComponent((activity!!.application as App).getAppComponent())
            .mainActivityContextModule(MainActivityContextModule(activity!!))
            .mainPresenterModule(MainPresenterModule(this))
            .build()
        mainComponent.injectFragment(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSoftInputStyle()
    }

    protected open fun setSoftInputStyle() {

        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun getProgressBar(): ProgressBar? {
        return null
    }

//    override fun userRetrieved(user: User) {}
//
//    override fun onMusicGenresReceived(musicData: MusicPropertiesDataModel) {}
//
//    override fun updateEvents(searchResult: SearchResult) {}
//
//    override fun updateNewsfeed(newsfeedList: ArrayList<NewsFeed>) {}
//
//    override fun updateSupportedCountries(supportedCountries: ArrayList<SupportedCountry>) {}
//
//    override fun updateCities(cities: ArrayList<City>) {}
//
//    override fun updatePhotos(userPictures: UserPictures) {}
//
//    override fun updateUserFriends(userFriends: UserFriends) {}
//
//    override fun updatePastEvents(places: ArrayList<Place>) {}
//
//    override fun updatePictureDetails(pictureDetails: PictureDetails) {}
//
//    override fun updatePlaceDetails(place: Place) {
//        when (place.placeDefinitionId) {
//            2 -> (activity as MainActivity).openEventPlaceFragment(place)
//            else -> (activity as MainActivity).openClubPlaceFragment(place)
//        }
//    }
//
//    override fun updateComments(comments: ArrayList<Comment>) {}
//
//    override fun updatePeopleGoing(peopleGoing: ArrayList<PeopleGoing>) {}
//
//    override fun updateRatings(ratings: ArrayList<Rating>) {}
//
//    override fun updateView() {
//
//    }
//
//    override fun updateWebView(webViewData: WebViewData?) {}
//
//    override fun updateCheckContacts(friends: ArrayList<Friend>) {}
//
//    override fun onFriendAdded(friendId: String) {
//        RxBus.publish(UpdateFriendListEvent(friendId, FriendStatusTypes.FRIEND_REQUESTED_BY_CURRENT))
//    }
//
//    override fun onFriendConfirmed() {}
//
//    override fun onFriendInvited() {}
//
//    open fun dataLoaded() {}
//
//    override fun onUserInvitedToEvent() {}
//
//    override fun displayUser(editUserProfileResponse: EditUserProfileResponse) {}
//
//    override fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {}
//
//    override fun onGetMyFriendListCompleted(userFriends: UserFriends) {}
//
//    override fun onEditedUsernameIsUnique(newNickname: String) {}
//
//    override fun onLanguageUpdated() {}
//
//    override fun onBannersReceived(bannerDataResponse: BannerDataResponse) {}

    override fun onStop() {
        super.onStop()
//        Utils.hideSoftKeyboard(this)
    }
//
//    override fun onNewsFeedLiked() {
//
//    }

    override fun onDestroy() {
        mainPresenter.unsubscribe()
        compositeDisposableContainer.clear()
        super.onDestroy()
    }

//    override fun onSuccessUpcomingEvents(events: ArrayList<Place>) {
//
//    }
//
//    override fun onPasswordChangeError() {
//
//    }

}