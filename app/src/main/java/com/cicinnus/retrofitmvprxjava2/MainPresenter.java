package com.cicinnus.retrofitmvprxjava2;

import android.app.Activity;

import com.cicinnus.retrofitlib.base.BaseMVPPresenter;
import com.cicinnus.retrofitlib.utils.SchedulersCompact;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by Cicinnus on 2017/3/28.
 */

public class MainPresenter extends BaseMVPPresenter<MainContract.IMainView> implements MainContract.IMainPresenter {

    private final MainModel mainModel;

    public MainPresenter(Activity activity, MainContract.IMainView view) {
        super(activity, view);
        mainModel = new MainModel();
    }

    @Override
    public void getMainData() {
        mView.showLoading();
        addSubscribeUntilDestroy(mainModel.getMainData()
                .compose(SchedulersCompact.<ResultBean>applyIoSchedulers())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean resultBean) throws Exception {
                        mView.addMainData(resultBean.getDate());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mView.showError(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.showContent();
                    }
                }));
    }
}