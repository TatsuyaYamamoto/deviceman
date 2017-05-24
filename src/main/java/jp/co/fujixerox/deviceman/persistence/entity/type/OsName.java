package jp.co.fujixerox.deviceman.persistence.entity.type;

public enum OsName {
    iOS, Android;

    /**
     * get Enum value in "insensitively";
     *
     * @param name required enum name.
     * @return
     */
    public static OsName forName(String name) {
        for (OsName osName : OsName.values()) {
            if (osName.name().toLowerCase().equals(name.toLowerCase())) {
                return osName;
            }
        }
        return null;
    }
}
