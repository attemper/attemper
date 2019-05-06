package com.github.attemper.common.enums;

/**
 * @author ldang
 */
public enum UriType {

    DiscoveryClient(0),

    IpWithPort(1),

    DomainName(2);

    private int type;

    UriType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static UriType get(int type) {
        for (UriType item : UriType.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
}
