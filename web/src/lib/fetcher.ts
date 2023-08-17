import router from "../routes"

export function fetchJsonWithAuth<T>(url: string, data?: any): Promise<T> {
    return data
        ? fetch(
            url,
            {
                headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("accessToken") },
                body: data
            })
            .then(async (e) => {
                if (e.status === 401) await router.push({ path: '/' })
                return e
            })
            .then<T>((e) => e.json())
        : fetch(
            url,
            {
                headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
            })
            .then(async (e) => {
                if (e.status === 401) await router.push({ path: '/' })
                return e
            })
            .then<T>((e) => e.json())
}

export function fetchWithAuth(url: string): Promise<any> {
    return fetch(
        url,
        {
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken") },
        })
        .then(async (e) => {
            if (e.status === 401) await router.push({ path: '/' })
            return e
        })

}

export function sendForm(url: string, formdata: HTMLFormElement, method: string): Promise<any> {
    const entries = new FormData(formdata).entries();
    const data = Object.fromEntries(entries);
    return fetch(
        url,
        {
            method,
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken"), "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
        .then(async (e) => {
            if (e.status === 401) await router.push({ path: '/' })
            return e
        })
}

export function sendJson(url: string, data: any, method: string): Promise<any> {
    return fetch(
        url,
        {
            method,
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken"), "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
        .then(async (e) => {
            if (e.status === 401) await router.push({ path: '/' })
            return e
        })
}