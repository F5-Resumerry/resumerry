package com.f5.resumerry.Member.dto.ValidationGroups;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroups.NotEmptyGroup.class, ValidationGroups.PatternCheckGroup.class, })
public interface ValidationSequence {
}
