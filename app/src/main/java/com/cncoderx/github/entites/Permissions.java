package com.cncoderx.github.entites;

/**
 * @author cncoderx
 */

public class Permissions {
    private boolean admin;
    private boolean push;
    private boolean pull;

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public boolean getAdmin() {
        return admin;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
    public boolean getPush() {
        return push;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }
    public boolean getPull() {
        return pull;
    }
}
