package io.nearby.android.ui.newspotted;

import android.support.annotation.Nullable;

import java.io.File;

import io.nearby.android.data.manager.ClientManager;
import io.nearby.android.data.manager.SpottedManager;
import io.nearby.android.data.model.Location;
import io.nearby.android.data.model.Spotted;
import io.nearby.android.util.ImageUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NewSpottedPresenter implements NewSpottedContract.Presenter {

    private NewSpottedContract.View mView;

    public NewSpottedPresenter(NewSpottedContract.View view) {
        this.mView = view;
    }

    @Override
    public void createSpotted(double lat,
                              double lng,
                              String message,
                              boolean anonymity,
                              @Nullable File file) {
        mView.showSendingProgressDialog();

        final File compressPicture;
        Location location = new Location(lat, lng);

        Spotted spotted = new Spotted(Spotted.DEFAULT_ID,
                message,
                location,
                anonymity);

        if (file != null) {
            compressPicture = ImageUtil.compressBitmap(file);
        } else {
            compressPicture = null;
        }

        SpottedManager.createSpotted(spotted, compressPicture)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(o -> mView.onSpottedCreated())
                .doOnError(throwable -> mView.onSpottedNotCreated())
                .doOnComplete(() -> {
                    mView.hideSendingProgressDialog();
                    if (compressPicture != null) {
                        compressPicture.delete();
                    }
                })
                .subscribe();
    }

    @Override
    public boolean getDefaultAnonymity() {
        return ClientManager.getDefaultAnonymity();
    }

    @Override
    public void updateDefaultAnonymity(boolean anonymity) {
        ClientManager.setDefaultAnonymity(anonymity);
    }
}
