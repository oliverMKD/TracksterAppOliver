package com.trackster.tracksterapp.main

import com.trackster.tracksterapp.base.AppComponent
import com.trackster.tracksterapp.base.BaseMainViewFragment
import com.trackster.tracksterapp.base.FragmentScope
import com.trackster.tracksterapp.main.fragments.LoadsFragment
import com.trackster.tracksterapp.main.fragments.SettingsFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [MainPresenterModule::class])
interface MainComponent {

    fun injectFragment(baseMainViewFragment: BaseMainViewFragment)

    fun injectSettingsfragment(settingsFragment: SettingsFragment )

    fun injectLoadsFragment ( loadsFragment: LoadsFragment)
//
//    fun injectFragment(eventsFragment: EventsFragment)
//
//    fun injectFragment(menuFragment: MenuFragment)
//
//    fun injectFragment(profileFragment: ProfileFragment)
//
//    fun injectFragment(photosFragment: PhotosFragment)
//
//    fun injectFragment(clubEventsFragment: ClubEventsFragment)
//
//    fun injectFragment(pictureFragment: PictureFragment)
//
//    fun injectFragment(commentsFragment: CommentsFragment)
//
//    fun injectFragment(peopleGoingFragment: PeopleGoingFragment)
//
//    fun injectFragment(feedbackFragment: FeedbackFragment)
//
//    fun injectFragment(userFragment: UserFragment)
//
//    fun injectFragment(tagPeopleFragment: TagPeopleFragment)
//
//    fun injectFragment(leaveFeedbackFragment: LeaveFeedbackFragment)
//
//    fun injectFragment(eventPlaceFragment: EventPlaceFragment)
//
//    fun injectFragment(clubPlaceFragment: ClubPlaceFragment)
//
//    fun injectFragment(staticFragment: StaticFragment)
//
//    fun injectFragment(reportAbuseFragment: ReportAbuseFragment)
//
//    fun injectFragment(inviteFragment: InviteFragment)
//
//    fun injectFragment(friendsFragment: FriendsFragment)
//
//    fun injectFragment(addFriendFragment: AddFriendFragment)
//
//    fun injectSettingsfragment(settingsFragment: SettingsFragment )
//
//    fun injectNameFragment (nameFragment: NameFragment)
//
//    fun injectNickNameFragment (nicknameFragment: NicknameFragment)
//
//    fun injectBirthdayFragment (birthdayFragment: BirthdayFragment)
//
//    fun injectPhoneFragment (phoneFragment: PhoneFragment)
//
//    fun injectEmailFragment (emailFragment: EmailFragment)
//
//    fun injectPasswordFragment (passwordFragment: PasswordFragment)
//
//    fun injectFragment(inviteUserFragment: InviteUserFragment)
//
//    fun injectFragment(baseMainViewFragment: BaseMainViewFragment)

}