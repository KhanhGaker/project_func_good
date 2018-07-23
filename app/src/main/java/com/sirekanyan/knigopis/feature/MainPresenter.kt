package com.sirekanyan.knigopis.feature

import com.sirekanyan.knigopis.common.BasePresenter
import com.sirekanyan.knigopis.common.Presenter
import com.sirekanyan.knigopis.common.extensions.io2main
import com.sirekanyan.knigopis.common.functions.logError
import com.sirekanyan.knigopis.model.CurrentTab
import com.sirekanyan.knigopis.model.CurrentTab.*
import com.sirekanyan.knigopis.repository.BookRepository
import com.sirekanyan.knigopis.repository.Configuration
import com.sirekanyan.knigopis.repository.NoteRepository
import com.sirekanyan.knigopis.repository.UserRepository
import io.reactivex.Flowable

interface MainPresenter : Presenter {

    fun showPage(tab: CurrentTab, isForce: Boolean)

    interface Router {
        fun login()
        fun openProfileScreen()
        fun reopenScreen()
        fun openNewBookScreen()
    }

}

class MainPresenterImpl(
    private val view: MainView,
    private val router: MainPresenter.Router,
    private val config: Configuration,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository
) : BasePresenter(), MainPresenter, MainView.Callbacks {

    private val loadedTabs = mutableSetOf<CurrentTab>()

    override fun showPage(tab: CurrentTab, isForce: Boolean) {
        view.showPage(tab)
        val isFirst = !loadedTabs.contains(tab)
        if (isFirst || isForce) {
            when (tab) {
                HOME_TAB -> refreshHomeTab(tab)
                USERS_TAB -> refreshUsersTab(tab)
                NOTES_TAB -> refreshNotesTab(tab)
            }
        }
    }

    override fun onLoginOptionClicked() {
        router.login()
    }

    override fun onProfileOptionClicked() {
        router.openProfileScreen()
    }

    override fun onAboutOptionClicked() {
        view.showAboutDialog()
    }

    override fun onDarkThemeOptionClicked(isChecked: Boolean) {
        config.isDarkTheme = isChecked
        router.reopenScreen()
    }

    override fun onAddBookClicked() {
        router.openNewBookScreen()
    }

    private fun refreshHomeTab(tab: CurrentTab) {
        bookRepository.observeBooks()
            .io2main()
            .showProgressBar()
            .bind({ books ->
                view.updateBooks(books)
                loadedTabs.add(tab)
            }, {
                logError("cannot load books", it)
                view.showBooksError(it)
            })
    }

    private fun refreshUsersTab(tab: CurrentTab) {
        userRepository.observeUsers()
            .io2main()
            .showProgressBar()
            .bind({ users ->
                view.updateUsers(users)
                loadedTabs.add(tab)
            }, {
                logError("cannot load users", it)
                view.showUsersError(it)
            })
    }

    private fun refreshNotesTab(tab: CurrentTab) {
        noteRepository.observeNotes()
            .io2main()
            .showProgressBar()
            .bind({ notes ->
                view.updateNotes(notes)
                loadedTabs.add(tab)
            }, {
                logError("cannot load notes", it)
                view.showNotesError(it)
            })
    }

    private fun <T> Flowable<T>.showProgressBar(): Flowable<T> =
        doOnSubscribe {
            view.showProgress()
        }.doOnNext {
            view.hideProgress()
        }.doFinally {
            view.hideProgress()
            view.hideSwipeRefresh()
        }

}