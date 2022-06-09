// let {
//   publicRuntimeConfig
// } = getConfig();

let publicRuntimeConfig = {
	appConfig: {
		callName: 'hzAppCallFn',
		callAndroid: 'android',
	},
};
// console.log('window', window)

function getCallbackName(name) {
	return `${name}_cb`;
}

function isBrowser() {
        let userAgent = window.navigator.userAgent;
        let isAndroid = userAgent.indexOf('Android') > -1 || userAgent.indexOf('Adr') > -1; // android终端
        let isiOS = !!userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
        if (isAndroid) {
                // console.dir("是在安卓手机的微信浏览器里");
                return 'android';
        } else if (isiOS) {
                // console.dir("是在ios手机里的微信浏览器里");
                return 'ios';
        }
        return '';
}

/**
 * 注册事件
 *
 * @param name 事件名称 string
 * @param fn 函数方法 func (obj, cb)
 */
function register(name, fn) {
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
function registerOff(name) {
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
function trigger(name, param, cb) {
	if (typeof window === 'undefined') {
		return new Error(
			'Please place the registration method in the customer service life cycle.',
		);
	}
	let browser = isBrowser();
	try {
		let newParam;
		if (param && typeof param !== 'string') {
			let newObj = {
				...param,
			};
			if (typeof cb === 'function') {
				let callbackName = getCallbackName(name);
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
