export function fetchJsonWithAuth<T>(url, data): Promise<T> {
    return fetch(
        url,
        {
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + localStorage.getItem("accessToken") },
            body: data
        })
        .then<T>((e) => e.json())
}

export function fetchWithAuth<T>(url): Promise<T> {
    return fetch(
        url,
        {
            headers: {"Authorization": "Bearer " + localStorage.getItem("accessToken") },
        })
        .then<T>((e) => e.json())
}

export default { fetchJsonWithAuth, fetchWithAuth }