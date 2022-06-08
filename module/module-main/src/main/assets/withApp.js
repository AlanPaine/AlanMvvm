/*
 * @Description: Description
 * @Author: wuchuan 1614
 * @Date: 2021-03-25 13:43:09
 * @LastEditTime: 2022-06-01 11:28:55
 * @LastEditors: ningheng
 * @Reference:
 */
// import getConfig from "next/config";

import { isBrowser } from './utils';

// const {
//   publicRuntimeConfig
// } = getConfig();

const publicRuntimeConfig = {
	appConfig: {
		callName: 'hzAppCallFn',
		callAndroid: 'android',
	},
};
// console.log('window', window)

function getCallbackName(name) {
	return `${name}_cb`;
}

/**
 * 注册事件
 *
 * @param name 事件名称 string
 * @param fn 函数方法 func (obj, cb)
 */
export function register(name, fn) {
	if (typeof name !== 'string') {
		return new Error('First param must be a string.');
	} else if (typeof fn !== 'function') {
		return new Error('Second param must be a function.');
	} else if (typeof window === 'undefined') {
		return new Error(
			'Please place the registration method in the customer service life cycle.',
		);
	}
	if (typeof window[publicRuntimeConfig.appConfig.callName] !== 'object') {
		window[publicRuntimeConfig.appConfig.callName] = {};
	}
	if (!window[publicRuntimeConfig.appConfig.callName][name]) {
		window[publicRuntimeConfig.appConfig.callName][name] = jsonStr => {
			let cbFn;
			let data;
			try {
				data = JSON.parse(jsonStr);
				if (typeof data.callback === 'string') {
					cbFn = a => {
						// eslint-disable-next-line
						trigger(data.callback, a);
					};
				}
			} catch (e) {
				data = jsonStr;
			}
			fn(data, cbFn);
		};
	}
}

/**
 * 注销事件
 *
 * @param name 事件名称 string
 */
export function registerOff(name) {
	if (typeof name !== 'string') {
		return new Error('First param must be a string.');
	} else if (typeof window === 'undefined') {
		return new Error(
			'Please place the registration method in the customer service life cycle.',
		);
	}
	if (typeof window[publicRuntimeConfig.appConfig.callName] === 'object') {
		delete window[publicRuntimeConfig.appConfig.callName][getCallbackName(name)];
		return delete window[publicRuntimeConfig.appConfig.callName][name];
	}
}

/**
 * 触发事件
 *
 * @param name 事件名称 string
 * @param param 参数对象 string | object | undefined
 * @param cb 回调方法 func | undefined (注意，使用回调的时候param需要是对象)
 */
export function trigger(name, param, cb) {
	if (typeof window === 'undefined') {
		return new Error(
			'Please place the registration method in the customer service life cycle.',
		);
	}
	const browser = isBrowser();
	try {
		let newParam;
		if (param && typeof param !== 'string') {
			const newObj = {
				...param,
			};
			if (typeof cb === 'function') {
				const callbackName = getCallbackName(name);
				newObj.callback = callbackName;
				// 需要优化，合理注销事件
				register(callbackName, data => {
					registerOff(callbackName);
					cb(data);
				});
			}
			newParam = JSON.stringify(newObj);
		} else {
			newParam = param;
		}
		if (browser === 'android') {
			// console.log(
			// 	'window[publicRuntimeConfig.appConfig.callAndroid][name]------------',
			// 	window[publicRuntimeConfig.appConfig.callAndroid],
			// );
			// alert(window[publicRuntimeConfig.appConfig.callAndroid])
			// alert(JSON.stringify(window[publicRuntimeConfig.appConfig.callAndroid][name]))
			window[publicRuntimeConfig.appConfig.callAndroid][name](newParam);
		} else if (browser === 'ios') {
			window.webkit.messageHandlers[name].postMessage(newParam);
		}
	} catch (e) {
		// eslint-disable-next-line
		console.trace(e);
		return false;
	}
	return true;
}
