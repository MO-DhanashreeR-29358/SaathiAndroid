package com.mosl.mobile.domain.ausfbLead

import com.mosl.saathi.api.model.dashboard.ausfbLead.BankCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.BranchCodeModel
import com.mosl.saathi.api.model.dashboard.ausfbLead.SubBranchCodeModel


@JvmField
val SelectBank =
    BankCodeModel(-1, "Select Bank")
@JvmField
val SelectBranch =
    BranchCodeModel(-1, "Select Branch", -1)
@JvmField
val SelectSubBranch =
    SubBranchCodeModel(-1, "Select Sub Branch")