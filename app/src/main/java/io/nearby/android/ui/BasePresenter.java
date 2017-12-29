package io.nearby.android.ui;


/**
 * Created by Marc on 2017-03-09
 */

public class BasePresenter {

    public static boolean manageError(BaseView view){
        boolean handled = false;

//        if(errorType == SpottedDataSource.ErrorType.DisabledUser){
//            handled = true;
//            view.onUserAccountDisabled();
//        }
//        else if(errorType == SpottedDataSource.ErrorType.UnauthorizedUser){
//            handled = true;
//            view.onUserUnauthorized();
//        }

        return handled;
    }
}
