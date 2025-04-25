package com.portalSite.common.valid;

import com.portalSite.cafe.dto.CafeLevelRequest;
import com.portalSite.cafe.dto.CafeLevelRequestList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoDuplicateGradeOrderValidator implements ConstraintValidator<NoDuplicateGradeOrder, CafeLevelRequestList> {

    @Override
    public boolean isValid(CafeLevelRequestList cafeLevelRequestList, ConstraintValidatorContext constraintValidatorContext) {
        if (cafeLevelRequestList == null || cafeLevelRequestList.cafeLevelRequestList() == null) {
            return true;
        }

        List<Integer> orders = cafeLevelRequestList.cafeLevelRequestList().stream()
                .map(CafeLevelRequest::gradeOrder).toList();

        Set<Integer> uniqueOrders = new HashSet<>(orders);

        return uniqueOrders.size() ==orders.size();
    }
}
