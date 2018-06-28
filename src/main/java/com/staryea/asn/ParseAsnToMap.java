package com.staryea.asn;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParseAsnToMap {
    private Stack tagStack = new Stack();
    private String separator = new String(",");

    private Map<String, String> valueMap = new TreeMap();
    private Map<String, String> fieldType = new TreeMap();
    private static final int SAMPLE_SIZE = 32;
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyMMddHHmmss");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public ParseAsnToMap(Map<String, String> fieldType)
    {
        this.fieldType = fieldType;
    }

    public Map<String, String> dumpAsMap(Object obj)
            throws Exception
    {
        if ((obj instanceof ASN1Primitive))
            _dumpAsString((ASN1Primitive)obj);
        else if ((obj instanceof ASN1Encodable))
            _dumpAsString(((ASN1Encodable)obj).toASN1Primitive());
        else {
            throw new Exception("not support Object Type !" +
                    obj.getClass().getName());
        }
        return this.valueMap;
    }

    public Map<String, String> dumpAsMap2(Object obj)
            throws Exception
    {
        if ((obj instanceof ASN1Primitive))
            _dumpAsString((ASN1Primitive)obj);
        else if ((obj instanceof ASN1Encodable))
            _dumpAsString(((ASN1Encodable)obj).toASN1Primitive());
        else {
            throw new Exception("not support Object Type !" +
                    obj.getClass().getName());
        }
        return this.valueMap;
    }

    private void _dumpAsString(ASN1Primitive obj)
            throws Exception
    {
        if ((obj instanceof ASN1TaggedObject)) {
            ASN1TaggedObject o = (ASN1TaggedObject)obj;
            this.tagStack.push(Integer.valueOf(o.getTagNo()));
            if (!o.isEmpty())
            {
                _dumpAsString(o.getObject());
            }
            this.tagStack.pop();
        } else {
            getDeepValue(obj);
        }
    }

    public String getPriValue(String tagKey, Object obj)
            throws Exception
    {
        String result = "";

        if ((obj instanceof ASN1OctetString)) {
            ASN1OctetString oct = (ASN1OctetString)obj;
            result = dumpBinaryDataAsString(tagKey, oct.getOctets());
        }
        else if ((obj instanceof ASN1ObjectIdentifier))
        {
            result = ((ASN1ObjectIdentifier)obj).getId();
        }
        else if ((obj instanceof ASN1Boolean))
        {
            result = ((ASN1Boolean)obj).toString();
        }
        else if ((obj instanceof ASN1Integer))
        {
            result = ((ASN1Integer)obj).toString();
        }
        else if ((obj instanceof DERBitString)) {
            DERBitString bt = (DERBitString)obj;

            result = dumpBinaryDataAsString(tagKey, bt.getBytes());
        }
        else if ((obj instanceof DERIA5String))
        {
            result = ((DERIA5String)obj).getString();
        }
        else if ((obj instanceof DERUTF8String))
        {
            result = ((DERUTF8String)obj).getString();
        }
        else if ((obj instanceof DERPrintableString))
        {
            result = ((DERPrintableString)obj).getString();
        }
        else if ((obj instanceof DERVisibleString))
        {
            result = ((DERVisibleString)obj).getString();
        }
        else if ((obj instanceof DERBMPString))
        {
            result = ((DERBMPString)obj).getString();
        } else if ((obj instanceof DERT61String))
        {
            result = ((DERT61String)obj).getString();
        } else if ((obj instanceof DERGraphicString))
        {
            result = ((DERGraphicString)obj).getString();
        } else if ((obj instanceof DERVideotexString))
        {
            result = ((DERVideotexString)obj).getString();
        }
        else if ((obj instanceof ASN1UTCTime))
        {
            result = ((ASN1UTCTime)obj).getTime();
        }
        else if ((obj instanceof ASN1GeneralizedTime))
        {
            result = ((ASN1GeneralizedTime)obj).getTime();
        } else {
            if ((obj instanceof BERApplicationSpecific))
            {
                throw new Exception("暂时不支持BERApplicationSpecific");
            }
            if ((obj instanceof DERApplicationSpecific))
            {
                throw new Exception("暂时不支持DERApplicationSpecific");
            }if ((obj instanceof ASN1Enumerated))
            {
                ASN1Enumerated en = (ASN1Enumerated)obj;
                result = en.getValue().toString(); } else {
                if ((obj instanceof DERExternal))
                {
                    throw new Exception("暂时不支持DERApplicationSpecific");
                }
                result = obj.toString();
            }
        }
        return result;
    }

    public void getDeepValue(Object obj)
            throws Exception
    {
        if ((obj instanceof ASN1Sequence))
        {
            Enumeration e = ((ASN1Sequence)obj).getObjects();
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                if ((o == null) || (o.equals(DERNull.INSTANCE))) {
                    continue;
                }
                if ((o instanceof ASN1Primitive))
                    _dumpAsString((ASN1Primitive)o);
                else {
                    _dumpAsString(((ASN1Encodable)o).toASN1Primitive());
                }

            }

        }
        else if ((obj instanceof ASN1Set)) {
            Enumeration e = ((ASN1Set)obj).getObjects();
            while (e.hasMoreElements()) {
                Object o = e.nextElement();
                if (o == null) {
                    continue;
                }
                if ((o instanceof ASN1Primitive))
                    _dumpAsString((ASN1Primitive)o);
                else
                    _dumpAsString(((ASN1Encodable)o).toASN1Primitive());
            }
        }
        else {
            List lst = new ArrayList();
            Iterator stackIt = this.tagStack.iterator();
            while (stackIt.hasNext()) {
                lst.add(stackIt.next());
            }
            String tagKey = StringUtils.join(lst.toArray(), this.separator);
            String result = getPriValue(tagKey, obj);

            this.valueMap.put(tagKey, result);
        }
    }

    public String dumpBinaryDataAsString(String tagKey, byte[] bytes)
    {
        StringBuffer buf = new StringBuffer();

        if (this.fieldType.containsKey(tagKey))
        {
            if ("INTEGER".equals(this.fieldType.get(tagKey)))
                buf.append(Integer.parseInt(StringUtils.upperCase(
                        Strings.fromByteArray(Hex.encode(bytes))), 16));
            else if ("TETSTRING".equals(this.fieldType.get(tagKey)))
            {
                buf.append(decodeTETString(bytes));
            }
            else {
                for (int i = 0; i < bytes.length; i += 32) {
                    if (bytes.length - i > 32)
                        buf.append(calculateAscString(bytes, i, 32));
                    else {
                        buf.append(calculateAscString(bytes, i, bytes.length - i));
                    }
                }
            }
        }
        else {
            for (int i = 0; i < bytes.length; i += 32) {
                if (bytes.length - i > 32)
                    buf.append(calculateAscString(bytes, i, 32));
                else {
                    buf.append(calculateAscString(bytes, i, bytes.length - i));
                }
            }
        }

        return buf.toString();
    }

    private static String calculateAscString(byte[] bytes, int off, int len) {
        StringBuffer buf = new StringBuffer();

        for (int i = off; i != off + len; i++) {
            if ((bytes[i] >= 32) && (bytes[i] <= 126)) {
                buf.append((char)bytes[i]);
            }
        }

        return buf.toString();
    }

    public static String decode(String bytes)
    {
        String hexString = "0123456789ABCDEF";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);

        for (int i = 0; i < bytes.length(); i += 2)
            baos.write(hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1)));
        return new String(baos.toByteArray());
    }


    public static String decodeTETString(byte[] b)
    {
        StringBuffer buf = new StringBuffer();
        buf.append((b[0] & 0xF0) >> 4).append(b[0] & 0xF)
                .append((b[1] & 0xF0) >> 4).append(b[1] & 0xF)
                .append((b[2] & 0xF0) >> 4).append(b[2] & 0xF)
                .append((b[3] & 0xF0) >> 4).append(b[3] & 0xF)
                .append((b[4] & 0xF0) >> 4).append(b[4] & 0xF)
                .append((b[5] & 0xF0) >> 4).append(b[5] & 0xF);
        try {
            return sdf2.format(sdf1.parse(buf.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    public Stack getTagStack() {
        return this.tagStack;
    }

    public void setTagStack(Stack tagStack) {
        this.tagStack = tagStack;
    }

    public String getSeparator() {
        return this.separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
