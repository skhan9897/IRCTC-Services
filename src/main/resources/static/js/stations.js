let stations = [];

document.addEventListener(
    "DOMContentLoaded",
    async function () {

        await loadStations();

        initializeStationSearch();

    }
);


async function loadStations() {

    const container =
        document.getElementById(
            "stationsGrid"
        );

    try {

        stations =
            await API.getStations();

        if (
            !Array.isArray(stations) ||
            stations.length === 0
        ) {

            container.innerHTML = `
                <div class="loading">
                    No stations found.
                </div>
            `;

            return;
        }

        displayStations(stations);

    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <div class="loading">
                Unable to load stations.
            </div>
        `;
    }
}


function displayStations(list) {

    const container =
        document.getElementById(
            "stationsGrid"
        );

    container.innerHTML = "";

    if (!list.length) {

        container.innerHTML = `
            <div class="loading">
                No matching station found.
            </div>
        `;

        return;
    }

    list.forEach(station => {

        const card =
            document.createElement("div");

        card.className =
            "station-card";

        card.innerHTML = `

            <div class="station-icon">
                🚉
            </div>

            <span class="station-code">
                ${escapeHtml(
            station.stationCode ||
            station.code ||
            "-"
        )}
            </span>

            <h3>
                ${escapeHtml(
            station.stationName ||
            station.name ||
            "-"
        )}
            </h3>

            <p>
                ${escapeHtml(
            station.city ||
            "-"
        )}
            </p>
        `;

        container.appendChild(card);

    });
}


function initializeStationSearch() {

    const input =
        document.getElementById(
            "stationSearch"
        );

    if (!input) return;

    input.addEventListener(
        "input",
        function () {

            const value =
                this.value
                    .toLowerCase()
                    .trim();

            const filtered =
                stations.filter(
                    station => {

                        const text = `
                            ${station.stationName || ""}
                            ${station.stationCode || ""}
                            ${station.city || ""}
                        `.toLowerCase();

                        return text.includes(
                            value
                        );
                    }
                );

            displayStations(filtered);

        }
    );
}


function escapeHtml(value) {

    return String(value)

        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}