import { useState, useEffect } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
    const [count, setCount] = useState(0)
    const [status, setStatus] = useState<string>('loading...')

    useEffect(() => {
        fetch("/api/v1/status")
            .then(async (res) => {
                const ct = res.headers.get("content-type") || "";
                if (ct.includes("application/json")) {
                    return res.json();
                } else {
                    return res.text();
                }
            })
            .then((data) => setStatus(JSON.stringify(data)))
            .catch((err) => setStatus("Error: " + err));
    }, []);
    return (
        <>
            <div>
                <a href="https://vite.dev" target="_blank">
                    <img src={viteLogo} className="logo" alt="Vite logo" />
                </a>
                <a href="https://react.dev" target="_blank">
                    <img src={reactLogo} className="logo react" alt="React logo" />
                </a>
            </div>
            <h1>Vite + React</h1>
            <div className="card">
                <button onClick={() => setCount((c) => c + 1)}>
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.tsx</code> and save to test HMR
                </p>
            </div>

            <h2>API Status</h2>
            <pre>{status}</pre>

            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>
        </>
    )
}

export default App
