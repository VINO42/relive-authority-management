package com.relive.utils;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;

public class IPUtils {
    private IPUtils() {
    }

    /**
     * 检查IP是否在指定范围内
     *
     * @param inputIP
     * @param rangeStartIP
     * @param rangeEndIP
     * @return
     * @throws AddressStringException 引用依赖ipaddress
     */
    public static boolean checkIPIsInGivenRange(String inputIP, String rangeStartIP, String rangeEndIP)
            throws AddressStringException {
        IPAddress startIPAddress = new IPAddressString(rangeStartIP).getAddress();
        IPAddress endIPAddress = new IPAddressString(rangeEndIP).getAddress();
        IPAddressSeqRange ipRange = startIPAddress.toSequentialRange(endIPAddress);
        IPAddress inputIPAddress = new IPAddressString(inputIP).toAddress();
        return ipRange.contains(inputIPAddress);
    }

    /**
     * 检查IPv4是否在指定范围内
     *
     * @param inputIP
     * @param rangeStartIP
     * @param rangeEndIP
     * @return 引用依赖commons-ip-math
     */
    public static boolean checkIPv4IsInRange(String inputIP, String rangeStartIP, String rangeEndIP) {
        Ipv4 startIPAddress = Ipv4.of(rangeStartIP);
        Ipv4 endIPAddress = Ipv4.of(rangeEndIP);
        Ipv4Range ipRange = Ipv4Range.from(startIPAddress).to(endIPAddress);
        Ipv4 inputIPAddress = Ipv4.of(inputIP);
        return ipRange.contains(inputIPAddress);
    }

    /**
     * 检查IPv6是否在指定范围内
     *
     * @param inputIP
     * @param rangeStartIP
     * @param rangeEndIP
     * @return 引用依赖commons-ip-math
     */
    public static boolean checkIPv6IsInRange(String inputIP, String rangeStartIP, String rangeEndIP) {
        Ipv6 startIPAddress = Ipv6.of(rangeStartIP);
        Ipv6 endIPAddress = Ipv6.of(rangeEndIP);
        Ipv6Range ipRange = Ipv6Range.from(startIPAddress).to(endIPAddress);
        Ipv6 inputIPAddress = Ipv6.of(inputIP);
        return ipRange.contains(inputIPAddress);
    }

    /**
     * IP 转换成Long
     *
     * @param ip
     * @return
     */
    public static long ipToLong(String ip) {
        String[] ips = ip.split("\\.");
        return (Long.valueOf(ips[0]) << 24) + (Long.valueOf(ips[1]) << 16) + (Long.valueOf(ips[2]) << 8) + Long.valueOf(ips[3]);
    }

    public static String longToIp(Long ipLong) {
        StringBuilder builder = new StringBuilder();
        builder.append(ipLong >>> 24).append(".");
        builder.append((ipLong >>> 16) & 0xFF).append(".");
        builder.append((ipLong >>> 8) & 0xFF).append(".");
        builder.append(ipLong & 0xFF);
        return builder.toString();
    }

}
