package com.proteam.renew.utilitys

import com.proteam.renew.requestModels.TrainingListResponsce
import com.proteam.renew.requestModels.TrainingListResponsceItem

interface TrainingListner {
   fun traininglisten(position: TrainingListResponsceItem)
}