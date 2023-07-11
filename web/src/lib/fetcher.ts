export function fetchJsonWithAuth<T>(url: string, data?: any): Promise<T> {
    return data
        ? fetch(
            url,
            {
                headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("accessToken") },
                body: data
            })
            .then<T>((e) => e.json())
        : fetch(
            url,
            {
                headers: { "Authorization": "Bearer " + localStorage.getItem("accessToken") },
            })
            .then<T>((e) => e.json())
}

export function fetchWithAuth(url: string): Promise<any> {
    return fetch(
        url,
        {
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken") },
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
}

export function sendJson(url: string, data: any, method: string): Promise<any> {
    return fetch(
        url,
        {
            method,
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken"), "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
}