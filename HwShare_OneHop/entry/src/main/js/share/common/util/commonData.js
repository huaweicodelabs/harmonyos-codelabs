export default {
  getRequestAction(bundleName, abilityName, abilityType, actionSync, requestCode) {
    return {
      bundleName: bundleName,
      abilityName: abilityName,
      abilityType: abilityType,
      syncOption: actionSync,
      messageCode: requestCode
    };
  }
};
