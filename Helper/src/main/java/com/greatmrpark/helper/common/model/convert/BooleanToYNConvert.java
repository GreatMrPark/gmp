/*
 *  Copyright (c) 2018 DOUB, Inc.
 *  All right reserved.
 *  This software is the confidential and proprietary information of DOUB
 *  , Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with DOUB.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  Seo Jong Hwa 18. 1. 16 오전 11:35
 *
 */

package com.greatmrpark.helper.common.model.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * Created by redpunk on 2017-05-25.
 */
@Convert
public class BooleanToYNConvert implements AttributeConverter<Boolean, String> {
    public String convertToDatabaseColumn(Boolean value) {
        return (value == null || !value) ? "N" : "Y";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "Y".equals(value);
    }
}
