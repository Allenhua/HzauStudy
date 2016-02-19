/**
 * Created by Hua on 2016/1/22.
 */
public class TransferData {
    private String viewState;
    private String cookie;

    public TransferData(String viewState, String cookie) {
        this.viewState = viewState;
        this.cookie = cookie;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
