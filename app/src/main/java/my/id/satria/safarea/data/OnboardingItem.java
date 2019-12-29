package my.id.satria.safarea.data;

public class OnboardingItem {

    String title, description;
    int screenImg1, screenImg2;

    public OnboardingItem(String title, String description, int screenImg1, int screenImg2) {
        this.title = title;
        this.description = description;
        this.screenImg1 = screenImg1;
        this.screenImg2 = screenImg2;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setScreenImg1(int screenImg) {
        this.screenImg1 = screenImg1;
    }

    public void setScreenImg2(int screenImg2) {
        this.screenImg2 = screenImg2;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getScreenImg1() {
        return screenImg1;
    }

    public int getScreenImg2() {
        return screenImg2;
    }
}
