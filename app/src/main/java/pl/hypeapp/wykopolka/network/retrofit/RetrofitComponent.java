package pl.hypeapp.wykopolka.network.retrofit;

import javax.inject.Singleton;

import dagger.Component;
import pl.hypeapp.wykopolka.presenter.AddBookPresenter;
import pl.hypeapp.wykopolka.presenter.AddedBooksPresenter;
import pl.hypeapp.wykopolka.presenter.AllBooksPresenter;
import pl.hypeapp.wykopolka.presenter.BookPanelPresenter;
import pl.hypeapp.wykopolka.presenter.BookPresenter;
import pl.hypeapp.wykopolka.presenter.DemandQueuePresenter;
import pl.hypeapp.wykopolka.presenter.EditBookPresenter;
import pl.hypeapp.wykopolka.presenter.MyBooksPresenter;
import pl.hypeapp.wykopolka.presenter.NavigationDrawerPresenter;
import pl.hypeapp.wykopolka.presenter.ProfileSettingsPresenter;
import pl.hypeapp.wykopolka.presenter.RandomBookPresenter;
import pl.hypeapp.wykopolka.presenter.SearchBookPresenter;
import pl.hypeapp.wykopolka.presenter.SelectedBooksPresenter;
import pl.hypeapp.wykopolka.presenter.SignInPresenter;
import pl.hypeapp.wykopolka.presenter.StatisticPresenter;
import pl.hypeapp.wykopolka.presenter.WishListPresenter;

@Singleton
@Component(modules = {WykopolkaRetrofitModule.class})
public interface RetrofitComponent {

    void inject(BookPanelPresenter tiPresenter);

    void inject(SignInPresenter tiPresenter);

    void inject(BookPresenter tiPresenter);

    void inject(NavigationDrawerPresenter tiPresenter);

    void inject(AddedBooksPresenter tiPresenter);

    void inject(MyBooksPresenter tiPresenter);

    void inject(RandomBookPresenter tiPresenter);

    void inject(StatisticPresenter tiPresenter);

    void inject(SelectedBooksPresenter tiPresenter);

    void inject(ProfileSettingsPresenter tiPresenter);

    void inject(SearchBookPresenter tiPresenter);

    void inject(AddBookPresenter tiPresenter);

    void inject(EditBookPresenter tiPresenter);

    void inject(AllBooksPresenter tiPresenter);

    void inject(WishListPresenter tiPresenter);

    void inject(DemandQueuePresenter tiPresenter);
}
