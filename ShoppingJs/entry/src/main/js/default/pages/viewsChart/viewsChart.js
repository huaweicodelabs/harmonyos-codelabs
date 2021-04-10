export default {
    data: {
        temp: 0,
        lineData: [
            {
                strokeColor: '#0081ff',
                fillColor: '#cce5ff',
                data: [763, 550, 551, 554, 731, 654, 525, 696, 595, 628, 791, 505, 613, 575, 475, 553, 491, 680, 657, 716],
                gradient: true,
            }
        ],
        lineOps: {
            xAxis: {
                lineStylemin: 0,
                max: 20,
                display: false,
            },
            yAxis: {
                min: 0,
                max: 1000,
                display: false
            },
            series: {
                lineStyle: {
                    width: "5px",
                    smooth: true,
                },
                headPoint: {
                    shape: "circle",
                    size: 10,
                    strokeWidth: 1,
                    fillColor: '#ffffff',
                    strokeColor: "#2C4CFF",
                    display: true
                },
                loop: {
                    margin: 2,
                    gradient: true,
                },
            },
        },
    },
    onInit() {
        this.autoAddData();
    },
    addData() {
        this.$refs.linechart.append({
            serial: 0,
            data: [Math.floor(Math.random() * 400) + 400]
        })
    },
    autoAddData() {
        setInterval(() => {
            let temp = [Math.floor(Math.random() * 400) + 400];
            if (this.temp % 20 === 0) {
                this.lineData[0].data = [];
                this.lineData = this.lineData.slice();
            } else {
                this.lineData[0].data.push(temp);
                this.$refs.linechart.append({
                    serial: 0,
                    data: temp
                });
            }
            this.temp++;
        }, 800);
    }
}