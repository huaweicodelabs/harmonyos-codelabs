/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml?entry");
/******/ })
/************************************************************************/
/******/ ({

/***/ "../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml?entry":
/*!*******************************************************************************************************************!*\
  !*** d:/work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml?entry ***!
  \*******************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var $app_template$ = __webpack_require__(/*! !../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/json.js!../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/template.js!./index.hml */ "./lib/json.js!./lib/template.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml")
var $app_style$ = __webpack_require__(/*! !../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/json.js!../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/style.js!./index.css */ "./lib/json.js!./lib/style.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.css")
var $app_script$ = __webpack_require__(/*! !../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/script.js!../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/babel-loader?presets[]=D:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=D:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!../../../../../../../../../../Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/resource-reference-script.js!./index.js */ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=D:\\Program Files\\Huawei\\Dev3.42Sdk\\js\\2.2.0.56\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=D:\\Program Files\\Huawei\\Dev3.42Sdk\\js\\2.2.0.56\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.js")

$app_define$('@app-component/index', [], function($app_require$, $app_exports$, $app_module$) {

$app_script$($app_module$, $app_exports$, $app_require$)
if ($app_exports$.__esModule && $app_exports$.default) {
$app_module$.exports = $app_exports$.default
}

$app_module$.exports.template = $app_template$

$app_module$.exports.style = $app_style$

})
$app_bootstrap$('@app-component/index',undefined,undefined)

/***/ }),

/***/ "./lib/json.js!./lib/style.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.css":
/*!**********************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/json.js!d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/style.js!d:/work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.css ***!
  \**********************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  ".container": {
    "flexDirection": "column",
    "width": "100%",
    "height": "100%",
    "backgroundColor": "#000000"
  },
  ".container-top": {
    "position": "absolute",
    "marginTop": "20px"
  },
  ".container-appBar-left": {
    "marginLeft": "30px",
    "width": "86px"
  },
  ".container-image-left": {
    "height": "24px",
    "width": "24px"
  },
  ".container-txt": {
    "fontSize": "16px",
    "marginLeft": "16px",
    "color": "#ffffff"
  },
  ".container-appBar-right": {
    "marginLeft": "90px"
  },
  ".container-image-right": {
    "height": "24px",
    "width": "24px",
    "marginLeft": "20px"
  },
  ".dialog-main": {
    "width": "80%",
    "height": "16%",
    "marginBottom": "30px"
  },
  ".dialog-div": {
    "flexDirection": "column",
    "alignItems": "center"
  },
  ".inner-txt": {
    "flexDirection": "column",
    "alignItems": "center",
    "marginTop": "16px"
  },
  ".txt": {
    "fontSize": "16px"
  },
  ".inner-btn": {
    "height": "60px",
    "justifyContent": "space-around",
    "alignItems": "center"
  },
  ".btn-cancel": {
    "marginLeft": "10px"
  },
  ".btn-stop": {
    "marginLeft": "50px"
  },
  "#crop_image_content": {
    "position": "absolute",
    "textAlign": "center",
    "marginTop": "130px"
  },
  "#cropBox": {
    "position": "absolute"
  },
  "#crop_image_content #canvasOne": {
    "position": "absolute",
    "height": "300px",
    "width": "300px",
    "marginLeft": "30px",
    "marginRight": "30px",
    "marginBottom": "30px"
  },
  "#crop_image_content #mainBox": {
    "borderTopWidth": "3px",
    "borderRightWidth": "3px",
    "borderBottomWidth": "3px",
    "borderLeftWidth": "3px",
    "borderTopStyle": "solid",
    "borderRightStyle": "solid",
    "borderBottomStyle": "solid",
    "borderLeftStyle": "solid",
    "borderTopColor": "#FFFFFF",
    "borderRightColor": "#FFFFFF",
    "borderBottomColor": "#FFFFFF",
    "borderLeftColor": "#FFFFFF",
    "position": "absolute",
    "top": "0px",
    "left": "0px"
  },
  ".minBox": {
    "position": "absolute",
    "height": "15px",
    "width": "3px",
    "backgroundColor": "#FF0000"
  },
  ".left-up-top": {
    "top": "-3px",
    "left": "-3px",
    "height": "3px",
    "width": "15px"
  },
  ".left-up": {
    "top": "-3px",
    "left": "-3px"
  },
  ".right-up-top": {
    "right": "-3px",
    "top": "-3px",
    "height": "3px",
    "width": "15px"
  },
  ".right-up": {
    "right": "-3px",
    "top": "-3px"
  },
  ".left-down-bot": {
    "bottom": "-3px",
    "left": "-3px",
    "height": "3px",
    "width": "15px"
  },
  ".left-down": {
    "bottom": "-3px",
    "left": "-3px"
  },
  ".right-down-bot": {
    "right": "-3px",
    "bottom": "-3px",
    "height": "3px",
    "width": "15px"
  },
  ".right-down": {
    "bottom": "-3px",
    "right": "-3px"
  },
  ".container-bottom-first": {
    "flexDirection": "column",
    "position": "absolute",
    "marginTop": "210px"
  },
  ".container-bottom-one-first": {
    "flexDirection": "row",
    "marginLeft": "50px",
    "marginTop": "240px"
  },
  ".container-bottom-image": {
    "height": "30px",
    "width": "30px",
    "marginLeft": "49px"
  },
  ".container-bottom-two-first": {
    "flexDirection": "column",
    "position": "relative",
    "marginLeft": "168px",
    "marginTop": "38px"
  },
  ".container-bottom-three-first": {
    "flexDirection": "row",
    "marginTop": "22px",
    "marginLeft": "80px"
  },
  ".container-bottom-four-first": {
    "flexDirection": "row",
    "position": "relative",
    "marginTop": "0px",
    "marginLeft": "80px"
  },
  ".container-bottom-second": {
    "flexDirection": "column",
    "position": "absolute",
    "marginTop": "450px"
  },
  ".container-bottom-two-second": {
    "flexDirection": "row",
    "marginTop": "30px"
  },
  ".container-bottom-three-second": {
    "flexDirection": "row"
  },
  ".brightnessTxt": {
    "fontSize": "14px",
    "marginLeft": "60px"
  },
  ".contrastTxt": {
    "fontSize": "14px",
    "marginLeft": "76px"
  },
  ".saturationTxt": {
    "fontSize": "14px",
    "marginLeft": "68px"
  },
  ".container-bottom-four-second": {
    "marginTop": "20px",
    "flexDirection": "row",
    "marginLeft": "80px"
  },
  ".container-bottom-five-second": {
    "flexDirection": "row",
    "marginLeft": "80px",
    "color": "#2788B9"
  }
}

/***/ }),

/***/ "./lib/json.js!./lib/template.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml":
/*!*************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/json.js!d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/template.js!d:/work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.hml ***!
  \*************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = {
  "attr": {
    "debugLine": "pages/index/index:16",
    "className": "container"
  },
  "type": "div",
  "classList": [
    "container"
  ],
  "children": [
    {
      "attr": {
        "debugLine": "pages/index/index:19",
        "className": "container-top"
      },
      "type": "div",
      "classList": [
        "container-top"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:20",
            "className": "container-appBar-left"
          },
          "type": "div",
          "classList": [
            "container-appBar-left"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:21",
                "src": "common/images/back.svg",
                "className": "container-image-left"
              },
              "type": "image",
              "classList": [
                "container-image-left"
              ],
              "onBubbleEvents": {
                "click": "showDialog"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:22",
                "className": "container-txt",
                "value": function () {return this.titleAppBar}
              },
              "type": "text",
              "classList": [
                "container-txt"
              ]
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:24",
            "className": "container-appBar-right"
          },
          "type": "div",
          "classList": [
            "container-appBar-right"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:25",
                "src": "common/images/redo.svg",
                "className": "container-image-right"
              },
              "type": "image",
              "classList": [
                "container-image-right"
              ],
              "onBubbleEvents": {
                "click": "redo"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:26",
                "src": "common/images/undo.svg",
                "className": "container-image-right"
              },
              "type": "image",
              "classList": [
                "container-image-right"
              ],
              "onBubbleEvents": {
                "click": "undo"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:27",
                "src": "common/images/save.svg",
                "className": "container-image-right"
              },
              "type": "image",
              "classList": [
                "container-image-right"
              ],
              "onBubbleEvents": {
                "click": "save"
              }
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:32",
        "id": "crop_image_content"
      },
      "type": "div",
      "id": "crop_image_content",
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:34",
            "id": "cropBox"
          },
          "type": "div",
          "id": "cropBox",
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:35",
                "id": "canvasOne"
              },
              "type": "canvas",
              "id": "canvasOne"
            },
            {
              "attr": {
                "debugLine": "pages/index/index:37",
                "id": "mainBox",
                "show": function () {return this.showFlag1}
              },
              "type": "div",
              "id": "mainBox",
              "style": {
                "width": function () {return this.cropWidth},
                "height": function () {return this.cropHeight},
                "top": function () {return this.cropTop},
                "left": function () {return this.cropLeft}
              },
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:40",
                    "id": "left-up-top",
                    "className": "minBox left-up-top"
                  },
                  "type": "div",
                  "id": "left-up-top",
                  "classList": [
                    "minBox",
                    "left-up-top"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:41",
                    "id": "left-up",
                    "className": "minBox left-up"
                  },
                  "type": "div",
                  "id": "left-up",
                  "classList": [
                    "minBox",
                    "left-up"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:42",
                    "id": "right-up-top",
                    "className": "minBox right-up-top"
                  },
                  "type": "div",
                  "id": "right-up-top",
                  "classList": [
                    "minBox",
                    "right-up-top"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:43",
                    "id": "right-up",
                    "className": "minBox right-up"
                  },
                  "type": "div",
                  "id": "right-up",
                  "classList": [
                    "minBox",
                    "right-up"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:44",
                    "id": "left-down-bot",
                    "className": "minBox left-down-bot"
                  },
                  "type": "div",
                  "id": "left-down-bot",
                  "classList": [
                    "minBox",
                    "left-down-bot"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:45",
                    "id": "left-down",
                    "className": "minBox left-down"
                  },
                  "type": "div",
                  "id": "left-down",
                  "classList": [
                    "minBox",
                    "left-down"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:46",
                    "id": "right-down-bot",
                    "className": "minBox right-down-bot"
                  },
                  "type": "div",
                  "id": "right-down-bot",
                  "classList": [
                    "minBox",
                    "right-down-bot"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:47",
                    "id": "right-down",
                    "className": "minBox right-down"
                  },
                  "type": "div",
                  "id": "right-down",
                  "classList": [
                    "minBox",
                    "right-down"
                  ]
                }
              ]
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:53",
        "className": "container-bottom-first",
        "show": function () {return this.showFlag1}
      },
      "type": "div",
      "classList": [
        "container-bottom-first"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:54",
            "className": "container-bottom-one-first"
          },
          "type": "div",
          "classList": [
            "container-bottom-one-first"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:56",
                "src": function () {return this.conBotFirImgSrc}
              },
              "type": "image",
              "style": {
                "width": "30px",
                "height": "30px"
              },
              "onBubbleEvents": {
                "click": "conBotFirImage"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:58",
                "src": function () {return this.conBotSecImgSrc},
                "className": "container-bottom-image"
              },
              "type": "image",
              "classList": [
                "container-bottom-image"
              ],
              "onBubbleEvents": {
                "click": "conBotSecImage"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:60",
                "src": function () {return this.conBotThrImgSrc},
                "className": "container-bottom-image"
              },
              "type": "image",
              "classList": [
                "container-bottom-image"
              ],
              "onBubbleEvents": {
                "click": "conBotThrImage"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:62",
                "src": function () {return this.conBotFouImgSrc},
                "className": "container-bottom-image"
              },
              "type": "image",
              "classList": [
                "container-bottom-image"
              ],
              "onBubbleEvents": {
                "click": "conBotFouImage"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:64",
            "className": "container-bottom-two-first"
          },
          "type": "div",
          "classList": [
            "container-bottom-two-first"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:66",
                "src": function () {return this.picFraImgSrc}
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:67",
                "value": function () {return this.picFrame}
              },
              "type": "text",
              "style": {
                "fontSize": "14px",
                "color": "#2788B9"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:69",
            "className": "container-bottom-three-first"
          },
          "type": "div",
          "classList": [
            "container-bottom-three-first"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:71",
                "src": "common/images/crop_blue.svg"
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:73",
                "src": "common/images/adjust_white.svg"
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px",
                "marginLeft": "148px"
              },
              "onBubbleEvents": {
                "click": "showAdjustPage"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:76",
            "className": "container-bottom-four-first"
          },
          "type": "div",
          "classList": [
            "container-bottom-four-first"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:78",
                "value": function () {return this.cropping}
              },
              "type": "text",
              "style": {
                "fontSize": "16px",
                "color": "#2788B9"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:80",
                "value": function () {return this.adjust}
              },
              "type": "text",
              "style": {
                "fontSize": "16px",
                "color": "#FFFFFF",
                "marginLeft": "138px"
              },
              "onBubbleEvents": {
                "click": "showAdjustPage"
              }
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:85",
        "className": "container-bottom-second",
        "show": function () {return this.showFlag2}
      },
      "type": "div",
      "classList": [
        "container-bottom-second"
      ],
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:86",
            "min": "1",
            "max": "10",
            "value": function () {return this.brightnessValue},
            "show": function () {return this.showBrightness}
          },
          "type": "slider",
          "events": {
            "change": "setBrightnessValue"
          },
          "style": {
            "color": "#808080"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:88",
            "min": "1",
            "max": "10",
            "value": function () {return this.contrastValue},
            "show": function () {return this.showContrast}
          },
          "type": "slider",
          "events": {
            "change": "setContrastValue"
          },
          "style": {
            "color": "#808080"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:90",
            "min": "1",
            "max": "10",
            "value": function () {return this.saturationValue},
            "show": function () {return this.showSaturation}
          },
          "type": "slider",
          "events": {
            "change": "setSaturationValue"
          },
          "style": {
            "color": "#808080"
          }
        },
        {
          "attr": {
            "debugLine": "pages/index/index:92",
            "className": "container-bottom-two-second"
          },
          "type": "div",
          "classList": [
            "container-bottom-two-second"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:94",
                "src": function () {return this.brightnessImgSrc}
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px",
                "marginLeft": "60px"
              },
              "onBubbleEvents": {
                "click": "brightnessAdj"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:98",
                "src": function () {return this.contrastImgSrc}
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px",
                "marginLeft": "86px"
              },
              "onBubbleEvents": {
                "click": "contrastAdj"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:102",
                "src": function () {return this.saturationImgSrc}
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px",
                "marginLeft": "86px"
              },
              "onBubbleEvents": {
                "click": "saturationAdj"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:105",
            "className": "container-bottom-three-second"
          },
          "type": "div",
          "classList": [
            "container-bottom-three-second"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:107",
                "className": "brightnessTxt",
                "value": function () {return this.brightness}
              },
              "type": "text",
              "classList": [
                "brightnessTxt"
              ],
              "style": {
                "color": function () {return this.brightnessColor}
              },
              "onBubbleEvents": {
                "click": "brightnessAdj"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:109",
                "className": "contrastTxt",
                "value": function () {return this.contrast}
              },
              "type": "text",
              "classList": [
                "contrastTxt"
              ],
              "style": {
                "color": function () {return this.contrastColor}
              },
              "onBubbleEvents": {
                "click": "contrastAdj"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:111",
                "className": "saturationTxt",
                "value": function () {return this.saturation}
              },
              "type": "text",
              "classList": [
                "saturationTxt"
              ],
              "style": {
                "color": function () {return this.saturationColor}
              },
              "onBubbleEvents": {
                "click": "saturationAdj"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:113",
            "className": "container-bottom-four-second"
          },
          "type": "div",
          "classList": [
            "container-bottom-four-second"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:115",
                "src": "common/images/crop_white.svg"
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px"
              },
              "onBubbleEvents": {
                "click": "showCropPage"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:117",
                "src": "common/images/adjust_blue.svg"
              },
              "type": "image",
              "style": {
                "width": "24px",
                "height": "24px",
                "marginLeft": "148px"
              }
            }
          ]
        },
        {
          "attr": {
            "debugLine": "pages/index/index:119",
            "className": "container-bottom-five-second"
          },
          "type": "div",
          "classList": [
            "container-bottom-five-second"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:121",
                "value": function () {return this.cropping}
              },
              "type": "text",
              "style": {
                "fontSize": "16px",
                "color": "#FFFFFF"
              },
              "onBubbleEvents": {
                "click": "showCropPage"
              }
            },
            {
              "attr": {
                "debugLine": "pages/index/index:123",
                "value": function () {return this.adjust}
              },
              "type": "text",
              "style": {
                "fontSize": "16px",
                "color": "#2788B9",
                "marginLeft": "138px"
              }
            }
          ]
        }
      ]
    },
    {
      "attr": {
        "debugLine": "pages/index/index:128",
        "id": "simpleDialog",
        "className": "dialog-main"
      },
      "type": "dialog",
      "id": "simpleDialog",
      "classList": [
        "dialog-main"
      ],
      "events": {
        "cancel": "cancelDialog"
      },
      "children": [
        {
          "attr": {
            "debugLine": "pages/index/index:129",
            "className": "dialog-div"
          },
          "type": "div",
          "classList": [
            "dialog-div"
          ],
          "children": [
            {
              "attr": {
                "debugLine": "pages/index/index:130",
                "className": "inner-txt"
              },
              "type": "div",
              "classList": [
                "inner-txt"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:131",
                    "className": "txt",
                    "value": function () {return this.title}
                  },
                  "type": "text",
                  "classList": [
                    "txt"
                  ]
                }
              ]
            },
            {
              "attr": {
                "debugLine": "pages/index/index:133",
                "className": "inner-btn"
              },
              "type": "div",
              "classList": [
                "inner-btn"
              ],
              "children": [
                {
                  "attr": {
                    "debugLine": "pages/index/index:134",
                    "type": "text",
                    "value": "取消",
                    "className": "btn-cancel"
                  },
                  "type": "button",
                  "onBubbleEvents": {
                    "click": "cancelSchedule"
                  },
                  "classList": [
                    "btn-cancel"
                  ]
                },
                {
                  "attr": {
                    "debugLine": "pages/index/index:135",
                    "type": "text",
                    "value": "放弃",
                    "className": "btn-stop"
                  },
                  "type": "button",
                  "onBubbleEvents": {
                    "click": "setSchedule"
                  },
                  "classList": [
                    "btn-stop"
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}

/***/ }),

/***/ "./lib/script.js!./node_modules/babel-loader/lib/index.js?presets[]=D:\\Program Files\\Huawei\\Dev3.42Sdk\\js\\2.2.0.56\\build-tools\\ace-loader\\node_modules\\@babel\\preset-env&plugins[]=D:\\Program Files\\Huawei\\Dev3.42Sdk\\js\\2.2.0.56\\build-tools\\ace-loader\\node_modules\\@babel\\plugin-transform-modules-commonjs&comments=false!./lib/resource-reference-script.js!../../../../../../../work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.js":
/*!*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************!*\
  !*** d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/script.js!d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/babel-loader/lib?presets[]=D:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/@babel/preset-env&plugins[]=D:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/node_modules/@babel/plugin-transform-modules-commonjs&comments=false!d:/Program Files/Huawei/Dev3.42Sdk/js/2.2.0.56/build-tools/ace-loader/lib/resource-reference-script.js!d:/work/harmonyos_codelabs-master/ImageEditorTemplate/entry/src/main/js/default/pages/index/index.js ***!
  \*******************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = function(module, exports, $app_require$){"use strict";

var _interopRequireDefault = __webpack_require__(/*! @babel/runtime/helpers/interopRequireDefault */ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js");

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports["default"] = void 0;

var _toConsumableArray2 = _interopRequireDefault(__webpack_require__(/*! @babel/runtime/helpers/toConsumableArray */ "./node_modules/@babel/runtime/helpers/toConsumableArray.js"));

var _system = _interopRequireDefault(requireModule("@system.app"));

var _system2 = _interopRequireDefault(requireModule("@system.prompt"));

var _default = {
  data: {
    title: '',
    titleAppBar: '',
    picFrame: '',
    cropping: '',
    adjust: '',
    picFraImgSrc: '',
    conBotFirImgSrc: '',
    conBotSecImgSrc: '',
    conBotThrImgSrc: '',
    conBotFouImgSrc: '',
    brightness: '',
    brightnessImgSrc: '',
    brightnessColor: '',
    contrast: '',
    contrastImgSrc: '',
    contrastColor: '',
    saturation: '',
    saturationImgSrc: '',
    saturationColor: '',
    cropWidth: 0,
    cropHeight: 0,
    cropTop: 38,
    cropLeft: 0,
    cropBoxLeftOne: 0,
    cropBoxLeftTwo: 0,
    cropBoxLeftThr: 0,
    cropBoxLeftFou: 0,
    brightnessValue: 10,
    oldBrightnessValue: 10,
    contrastValue: 10,
    oldContrastValue: 10,
    saturationValue: 10,
    oldSaturationValue: 10,
    beginBright: true,
    beginContrast: true,
    beginSaturation: true,
    brightnessImgData: null,
    contrastImgData: null,
    saturationImgData: null,
    dWidth: 0,
    dHeight: 0,
    ratios: 0,
    sx: 0,
    sy: 0,
    dx: 0,
    dy: 0,
    offset: 30,
    showFlag1: true,
    showFlag2: false,
    showBrightness: true,
    showContrast: false,
    showSaturation: false,
    canvasWidth: 300,
    canvasHeight: 300,
    originalImageTop: 0,
    originalImageWidth: 300,
    originalImageHeight: 224,
    cropImgMaxWidth: 300,
    cropImgMaxHeight: 300,
    zero: 0,
    two: 2,
    sliderMaxValue: 10
  },
  onInit: function onInit() {
    this.cropWidth = this.originalImageWidth;
    this.cropHeight = this.originalImageHeight;
    console.log('onInit()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.cropLeft = this.offset;
    this.title = this.$t('strings.title');
    this.titleAppBar = this.$t('strings.titleAppBar');
    this.picFrame = this.$t('strings.picFrame');
    this.cropping = this.$t('strings.cropping');
    this.adjust = this.$t('strings.adjust');
    this.brightness = this.$t('strings.brightness');
    this.contrast = this.$t('strings.contrast');
    this.saturation = this.$t('strings.saturation');
    this.picFraImgSrc = this.$t('strings.picFraImgSrc');
    this.conBotFirImgSrc = 'common/images/image_frame_white_blue.svg';
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    this.brightnessImgSrc = 'common/images/brightness_blue.svg';
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
  },
  onShow: function onShow() {
    var img = new Image();
    img.src = 'common/images/image.jpg';
    var el = this.$element('canvasOne');
    var ctx = el.getContext('2d');
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.originalImageTop = (this.canvasWidth - this.originalImageHeight) / this.two;
    ctx.drawImage(img, this.zero, this.originalImageTop, this.originalImageWidth, this.originalImageHeight);
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    this.cropBoxLeftOne = (this.originalImageWidth - this.originalImageWidth) / this.two + this.offset;
    this.cropBoxLeftTwo = (this.originalImageWidth - this.originalImageHeight) / this.two + this.offset;
    this.cropBoxLeftThr = (this.originalImageWidth - this.originalImageWidth) / this.two + this.offset;
    this.cropBoxLeftFou = (this.originalImageWidth - this.originalImageHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxTopOne = this.originalImageTop;
    this.cropBoxTopTwo = this.originalImageTop;
    this.cropBoxTopThr = (this.canvasHeight - this.originalImageWidth * 9 / 16) / this.two;
    this.cropBoxTopFou = this.originalImageTop;
    this.dWidth = this.originalImageWidth;
    this.dHeight = this.originalImageHeight;
    console.log('onShow()绘制完图片宽:' + this.dWidth + '高:' + this.dHeight);
  },
  showCropPage: function showCropPage() {
    this.showFlag1 = true;
    this.showFlag2 = false;

    switch (this.ratios) {
      case 0:
        this.conBotFirImage();
        break;

      case 1:
        this.conBotSecImage();
        break;

      case 16 / 9:
        this.conBotThrImage();
        break;

      case 9 / 16:
        this.conBotFouImage();
        break;

      default:
        break;
    }
  },
  showAdjustPage: function showAdjustPage() {
    this.showFlag1 = false;
    this.showFlag2 = true;
    this.showBrightness = true;
    this.showContrast = false;
    this.showSaturation = false;
    this.brightnessColor = '#2788B9';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#ffffff';

    switch (this.ratios) {
      case 0:
        break;

      case 1:
        this.cropOne();
        break;

      case 16 / 9:
        this.cropThr();
        break;

      case 9 / 16:
        this.cropFour();
        break;

      default:
        break;
    }
  },
  cropOne: function cropOne() {
    var el = this.$element('canvasOne');
    var ctx = el.getContext('2d');
    console.log('1:1比例裁剪cropOne()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.sx = (this.dWidth - this.cropWidth) / this.two;
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    var imageData;

    if (this.dHeight === this.cropImgMaxHeight) {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth + (this.cropImgMaxWidth - this.originalImageHeight * 9 / 16) / this.two, this.cropWidth);
    } else {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropWidth);
    }

    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.scale(this.cropImgMaxWidth / this.cropWidth, this.cropImgMaxHeight / this.cropHeight);
    this.dx = -(this.cropImgMaxWidth - this.dWidth) / this.two;
    this.dy = this.zero;
    console.log('1:1比例裁剪cropOne()ImageData对象:' + imageData);
    ctx.putImageData(imageData, this.dx, this.dy);
    ctx.scale(this.cropWidth / this.cropImgMaxWidth, this.cropHeight / this.cropImgMaxHeight);
    this.beginBright = this.brightnessValue === 0 ? true : false;
    this.beginContrast = this.contrastValue === 0 ? true : false;
    this.beginSaturation = this.saturationValue === 0 ? true : false;
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxLeftThr = this.offset;
    this.cropBoxLeftTwo = this.offset;
    this.cropBoxLeftOne = this.offset;
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    this.dWidth = this.cropImgMaxWidth;
    this.dHeight = this.cropImgMaxHeight;
    console.log('1:1比例裁剪cropOne()后图片宽:' + this.dWidth + '高:' + this.dHeight);
  },
  cropThr: function cropThr() {
    var el = this.$element('canvasOne');
    var ctx = el.getContext('2d');
    console.log('16:9比例裁剪cropThr()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.sx = (this.cropImgMaxWidth - this.dWidth) / this.two;
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    var imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropHeight);
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);

    if (this.dHeight === this.cropImgMaxHeight) {
      ctx.scale(this.cropImgMaxWidth / this.cropWidth, this.cropImgMaxWidth / this.cropWidth);
    }

    this.dy = (this.cropImgMaxHeight - this.cropHeight * (this.cropImgMaxWidth / this.cropWidth)) / (this.two * (this.cropImgMaxWidth / this.cropWidth));
    console.log('16:9比例裁剪cropThr()ImageData对象:' + imageData);
    ctx.putImageData(imageData, this.zero, this.dy);

    if (this.dHeight === this.cropImgMaxHeight) {
      ctx.scale(this.cropWidth / this.cropImgMaxWidth, this.cropWidth / this.cropImgMaxWidth);
    }

    this.beginBright = this.brightnessValue === 0 ? true : false;
    this.beginContrast = this.contrastValue === 0 ? true : false;
    this.beginSaturation = this.saturationValue === 0 ? true : false;
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxWidth * 9 / 16 * 9 / 16) / this.two + this.offset;
    this.cropBoxLeftThr = this.offset;
    this.cropBoxLeftTwo = (this.cropImgMaxWidth - this.cropImgMaxWidth * 9 / 16 * 1 / 1) / this.two + this.offset;
    this.cropBoxLeftOne = this.offset;
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    this.dWidth = this.cropImgMaxWidth;
    this.dHeight = this.cropImgMaxWidth * 9 / 16;
    console.log('16:9比例裁剪cropThr()后图片宽:' + this.dWidth + '高:' + this.dHeight);
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
  },
  cropFour: function cropFour() {
    var el = this.$element('canvasOne');
    var ctx = el.getContext('2d');
    console.log('9:16比例裁剪cropFour()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.sx = (this.dWidth - this.cropWidth) / this.two;
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    var imageData;

    if (this.dHeight === this.cropImgMaxHeight && this.dWidth !== this.cropImgMaxWidth) {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth + (this.cropImgMaxWidth - this.originalImageHeight * 9 / 16) / this.two, this.cropHeight);
    } else {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropHeight);
    }

    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);

    if (this.dWidth === this.cropImgMaxWidth) {
      ctx.scale(this.cropImgMaxHeight / this.cropHeight, this.cropImgMaxHeight / this.cropHeight);
    }

    if (this.dHeight === this.cropImgMaxHeight && this.dWidth !== this.cropImgMaxWidth) {
      this.dx = this.zero;
    } else {
      this.dx = (this.cropImgMaxWidth - this.cropWidth * (this.cropImgMaxHeight / this.cropHeight)) / (this.two * (this.cropImgMaxHeight / this.cropHeight));
    }

    this.dy = this.zero;
    console.log('9:16比例裁剪cropThr()ImageData对象:' + imageData);
    ctx.putImageData(imageData, this.dx, this.dy);

    if (this.dWidth === this.cropImgMaxWidth) {
      ctx.scale(this.cropHeight / this.cropImgMaxHeight, this.cropHeight / this.cropImgMaxHeight);
    }

    this.beginBright = this.brightnessValue === 0 ? true : false;
    this.beginContrast = this.contrastValue === 0 ? true : false;
    this.beginSaturation = this.saturationValue === 0 ? true : false;
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxLeftThr = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxLeftTwo = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxLeftOne = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 16 / 9) / this.two;
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16) / this.two;
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 9 / 16) / this.two;
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 16 / 9) / this.two;
    this.dHeight = this.cropImgMaxHeight;
    this.dWidth = this.dHeight * 9 / 16;
    console.log('9:16比例裁剪cropFour()后图片宽:' + this.dWidth + '高:' + this.dHeight);
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
  },
  conBotFirImage: function conBotFirImage() {
    this.conBotFirImgSrc = 'common/images/image_frame_white_blue.svg';
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    this.cropTop = this.cropBoxTopOne;
    this.cropLeft = this.cropBoxLeftOne;
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
    console.log('原图调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 0;
  },
  conBotSecImage: function conBotSecImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = 'common/images/ratios_1-1_blue.png';
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    this.cropLeft = this.cropBoxLeftTwo;

    if (this.dWidth < this.dHeight) {
      this.cropTop = (this.dHeight - this.dWidth) / this.two;
      this.cropWidth = this.dWidth;
      this.cropHeight = this.dWidth;
    } else {
      this.cropTop = this.cropBoxTopTwo;
      this.cropWidth = this.dHeight;
      this.cropHeight = this.dHeight;
    }

    console.log('1:1调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 1;
  },
  conBotThrImage: function conBotThrImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = 'common/images/ratios_16-9_blue.png';
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    this.cropTop = this.cropBoxTopThr;
    this.cropLeft = this.cropBoxLeftThr;
    this.cropWidth = this.dWidth;
    this.cropHeight = this.cropWidth * 9 / 16;
    console.log('16:9调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 16 / 9;
  },
  conBotFouImage: function conBotFouImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = 'common/images/ratios_9-16_blue.png';
    this.cropTop = this.cropBoxTopFou;
    this.cropLeft = this.cropBoxLeftFou;
    this.cropHeight = this.dHeight;
    this.cropWidth = this.dHeight * 9 / 16;
    console.log('9:16调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 9 / 16;
  },
  brightnessAdj: function brightnessAdj() {
    this.brightnessColor = '#2788B9';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#ffffff';
    this.brightnessImgSrc = 'common/images/brightness_blue.svg';
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
    this.showBrightness = true;
    this.showContrast = false;
    this.showSaturation = false;
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.brightnessImgData, this.zero, this.zero);
    this.brightnessValue = this.sliderMaxValue;
  },
  contrastAdj: function contrastAdj() {
    this.brightnessColor = '#ffffff';
    this.contrastColor = '#2788B9';
    this.saturationColor = '#ffffff';
    this.brightnessImgSrc = this.$t('strings.brightnessImgSrc');
    this.contrastImgSrc = 'common/images/contrast_blue.svg';
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
    this.showBrightness = false;
    this.showContrast = true;
    this.showSaturation = false;
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.contrastImgData, this.zero, this.zero);
    this.contrastValue = this.sliderMaxValue;
  },
  saturationAdj: function saturationAdj() {
    this.brightnessColor = '#ffffff';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#2788B9';
    this.brightnessImgSrc = this.$t('strings.brightnessImgSrc');
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = 'common/images/saturation_blue.svg';
    this.showBrightness = false;
    this.showContrast = false;
    this.showSaturation = true;
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.saturationImgData, this.zero, this.zero);
    this.saturationValue = this.sliderMaxValue;
  },
  setBrightnessValue: function setBrightnessValue(e) {
    if (e.mode === 'start') {
      this.oldBrightnessValue = e.value;
    } else if (e.mode === 'end') {
      this.brightnessValue = e.value;

      if (e.value === this.sliderMaxValue && this.beginBright) {
        var test = this.$element('canvasOne');
        var ctx = test.getContext('2d');
        ctx.restore();
        ctx.putImageData(this.brightnessImgData, this.zero, this.zero);
      } else {
        var adjustValue = e.value / this.oldBrightnessValue;
        console.log('adjustValue:: ' + adjustValue);
        this.adjustBrightness(adjustValue);
        this.oldBrightnessValue = e.value;
      }
    }
  },
  adjustBrightness: function adjustBrightness(value) {
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    var imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.putImageData(this.changeBrightness(imgData, value), this.zero, this.zero);
  },
  changeBrightness: function changeBrightness(imgdata, value) {
    var data = imgdata.data;

    for (var i = 0; i < data.length; i += 4) {
      var hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      hsv[2] *= value;
      var rgb = this.hsv2rgb((0, _toConsumableArray2["default"])(hsv));
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }

    return imgdata;
  },
  setContrastValue: function setContrastValue(e) {
    if (e.mode === 'start') {
      this.oldContrastValue = e.value;
    } else if (e.mode === 'end') {
      this.contrastValue = e.value;

      if (e.value === this.sliderMaxValue && this.beginContrast) {
        var test = this.$element('canvasOne');
        var ctx = test.getContext('2d');
        ctx.restore();
        ctx.putImageData(this.contrastImgData, this.zero, this.zero);
      } else {
        var adjustValue = e.value / this.oldContrastValue;
        this.adjustContrast(adjustValue);
        this.oldContrastValue = e.value;
      }
    }
  },
  adjustContrast: function adjustContrast(value) {
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    var imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.putImageData(this.changeContrast(imgData, value), this.zero, this.zero);
  },
  changeContrast: function changeContrast(imgdata, value) {
    var data = imgdata.data;

    for (var i = 0; i < data.length; i += 4) {
      var hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      hsv[0] *= value;
      var rgb = this.hsv2rgb((0, _toConsumableArray2["default"])(hsv));
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }

    return imgdata;
  },
  setSaturationValue: function setSaturationValue(e) {
    if (e.mode === 'start') {
      this.oldSaturationValue = e.value;
    } else if (e.mode === 'end') {
      this.saturationValue = e.value;

      if (e.value === this.sliderMaxValue && this.beginSaturation) {
        var test = this.$element('canvasOne');
        var ctx = test.getContext('2d');
        ctx.restore();
        ctx.putImageData(this.saturationImgData, this.zero, this.zero);
      } else {
        var adjustValue = e.value / this.oldSaturationValue;
        this.adjustSaturation(adjustValue);
        this.oldSaturationValue = e.value;
      }
    }
  },
  adjustSaturation: function adjustSaturation(value) {
    var test = this.$element('canvasOne');
    var ctx = test.getContext('2d');
    var imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.putImageData(this.changeSaturation(imgData, value), this.zero, this.zero);
  },
  changeSaturation: function changeSaturation(imgdata, value) {
    var data = imgdata.data;

    for (var i = 0; i < data.length; i += 4) {
      var hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      hsv[1] *= value;
      var rgb = this.hsv2rgb((0, _toConsumableArray2["default"])(hsv));
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }

    return imgdata;
  },
  rgb2hsv: function rgb2hsv(arr) {
    var rr;
    var gg;
    var bb;
    var r = arr[0] / 255;
    var g = arr[1] / 255;
    var b = arr[2] / 255;
    var h;
    var s;
    var v = Math.max(r, g, b);
    var diff = v - Math.min(r, g, b);

    var diffc = function diffc(c) {
      return (v - c) / 6 / diff + 1 / 2;
    };

    if (diff === 0) {
      h = s = 0;
    } else {
      s = diff / v;
      rr = diffc(r);
      gg = diffc(g);
      bb = diffc(b);

      if (r === v) {
        h = bb - gg;
      } else if (g === v) {
        h = 1 / 3 + rr - bb;
      } else if (b === v) {
        h = 2 / 3 + gg - rr;
      }

      if (h < 0) {
        h += 1;
      } else if (h > 1) {
        h -= 1;
      }
    }

    return [Math.round(h * 360), Math.round(s * 100), Math.round(v * 100)];
  },
  hsv2rgb: function hsv2rgb(hsv) {
    var _l = hsv[0];
    var _m = hsv[1];
    var _n = hsv[2];
    var newR;
    var newG;
    var newB;

    if (_m === 0) {
      _l = _m = _n = Math.round(255 * _n / 100);
      newR = _l;
      newG = _m;
      newB = _n;
    } else {
      _m = _m / 100;
      _n = _n / 100;
      var p = Math.floor(_l / 60) % 6;
      var f = _l / 60 - p;
      var a = _n * (1 - _m);
      var b = _n * (1 - _m * f);
      var c = _n * (1 - _m * (1 - f));

      switch (p) {
        case 0:
          newR = _n;
          newG = c;
          newB = a;
          break;

        case 1:
          newR = b;
          newG = _n;
          newB = a;
          break;

        case 2:
          newR = a;
          newG = _n;
          newB = c;
          break;

        case 3:
          newR = a;
          newG = b;
          newB = _n;
          break;

        case 4:
          newR = c;
          newG = a;
          newB = _n;
          break;

        case 5:
          newR = _n;
          newG = a;
          newB = b;
          break;
      }

      newR = Math.round(255 * newR);
      newG = Math.round(255 * newG);
      newB = Math.round(255 * newB);
    }

    return [newR, newG, newB];
  },
  redo: function redo() {
    _system2["default"].showToast({
      message: 'Please implement your redo function'
    });
  },
  undo: function undo() {
    _system2["default"].showToast({
      message: 'Please implement your undo function'
    });
  },
  save: function save() {
    _system2["default"].showToast({
      message: 'Please implement your save function'
    });
  },
  showDialog: function showDialog(e) {
    this.$element('simpleDialog').show();
  },
  cancelSchedule: function cancelSchedule(e) {
    this.$element('simpleDialog').close();
  },
  setSchedule: function setSchedule(e) {
    this.$element('simpleDialog').close();

    _system["default"].terminate();
  }
};
exports["default"] = _default;

function requireModule(moduleName) {
  const systemList = ['system.router', 'system.app', 'system.prompt', 'system.configuration',
  'system.image', 'system.device', 'system.mediaquery', 'ohos.animator', 'system.grid', 'system.resource']
  var target = ''
  if (systemList.includes(moduleName.replace('@', ''))) {
    target = $app_require$('@app-module/' + moduleName.substring(1));
    return target;
  }
  var shortName = moduleName.replace(/@[^.]+.([^.]+)/, '$1');
  if (typeof ohosplugin !== 'undefined' && /@ohos/.test(moduleName)) {
    target = ohosplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  if (typeof systemplugin !== 'undefined') {
    target = systemplugin;
    for (let key of shortName.split('.')) {
      target = target[key];
      if(!target) {
        break;
      }
    }
    if (typeof target !== 'undefined') {
      return target;
    }
  }
  target = requireNapi(shortName);
  return target;
}

var moduleOwn = exports.default || module.exports;
var accessors = ['public', 'protected', 'private'];
if (moduleOwn.data && accessors.some(function (acc) {
    return moduleOwn[acc];
  })) {
  throw new Error('For VM objects, attribute data must not coexist with public, protected, or private. Please replace data with public.');
} else if (!moduleOwn.data) {
  moduleOwn.data = {};
  moduleOwn._descriptor = {};
  accessors.forEach(function(acc) {
    var accType = typeof moduleOwn[acc];
    if (accType === 'object') {
      moduleOwn.data = Object.assign(moduleOwn.data, moduleOwn[acc]);
      for (var name in moduleOwn[acc]) {
        moduleOwn._descriptor[name] = {access : acc};
      }
    } else if (accType === 'function') {
      console.warn('For VM objects, attribute ' + acc + ' value must not be a function. Change the value to an object.');
    }
  });
}}
/* generated by ace-loader */


/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/arrayLikeToArray.js":
/*!*****************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/arrayLikeToArray.js ***!
  \*****************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function _arrayLikeToArray(arr, len) {
  if (len == null || len > arr.length) len = arr.length;

  for (var i = 0, arr2 = new Array(len); i < len; i++) {
    arr2[i] = arr[i];
  }

  return arr2;
}

module.exports = _arrayLikeToArray;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/arrayWithoutHoles.js":
/*!******************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/arrayWithoutHoles.js ***!
  \******************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var arrayLikeToArray = __webpack_require__(/*! ./arrayLikeToArray */ "./node_modules/@babel/runtime/helpers/arrayLikeToArray.js");

function _arrayWithoutHoles(arr) {
  if (Array.isArray(arr)) return arrayLikeToArray(arr);
}

module.exports = _arrayWithoutHoles;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/interopRequireDefault.js":
/*!**********************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/interopRequireDefault.js ***!
  \**********************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : {
    "default": obj
  };
}

module.exports = _interopRequireDefault;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/iterableToArray.js":
/*!****************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/iterableToArray.js ***!
  \****************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function _iterableToArray(iter) {
  if (typeof Symbol !== "undefined" && Symbol.iterator in Object(iter)) return Array.from(iter);
}

module.exports = _iterableToArray;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/nonIterableSpread.js":
/*!******************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/nonIterableSpread.js ***!
  \******************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function _nonIterableSpread() {
  throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
}

module.exports = _nonIterableSpread;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/toConsumableArray.js":
/*!******************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/toConsumableArray.js ***!
  \******************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var arrayWithoutHoles = __webpack_require__(/*! ./arrayWithoutHoles */ "./node_modules/@babel/runtime/helpers/arrayWithoutHoles.js");

var iterableToArray = __webpack_require__(/*! ./iterableToArray */ "./node_modules/@babel/runtime/helpers/iterableToArray.js");

var unsupportedIterableToArray = __webpack_require__(/*! ./unsupportedIterableToArray */ "./node_modules/@babel/runtime/helpers/unsupportedIterableToArray.js");

var nonIterableSpread = __webpack_require__(/*! ./nonIterableSpread */ "./node_modules/@babel/runtime/helpers/nonIterableSpread.js");

function _toConsumableArray(arr) {
  return arrayWithoutHoles(arr) || iterableToArray(arr) || unsupportedIterableToArray(arr) || nonIterableSpread();
}

module.exports = _toConsumableArray;

/***/ }),

/***/ "./node_modules/@babel/runtime/helpers/unsupportedIterableToArray.js":
/*!***************************************************************************!*\
  !*** ./node_modules/@babel/runtime/helpers/unsupportedIterableToArray.js ***!
  \***************************************************************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

var arrayLikeToArray = __webpack_require__(/*! ./arrayLikeToArray */ "./node_modules/@babel/runtime/helpers/arrayLikeToArray.js");

function _unsupportedIterableToArray(o, minLen) {
  if (!o) return;
  if (typeof o === "string") return arrayLikeToArray(o, minLen);
  var n = Object.prototype.toString.call(o).slice(8, -1);
  if (n === "Object" && o.constructor) n = o.constructor.name;
  if (n === "Map" || n === "Set") return Array.from(o);
  if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return arrayLikeToArray(o, minLen);
}

module.exports = _unsupportedIterableToArray;

/***/ })

/******/ });
//# sourceMappingURL=index.js.map