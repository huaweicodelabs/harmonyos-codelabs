import prompt from '@system.prompt';

export default {
    data: {
        red: 2.5,
        blue: 3,
        green: 1.5,
        avg: 2.3
    },
    rateRed(e) {
        this.red = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    },
    rateBlue(e) {
        this.blue = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    },
    rateGreen(e) {
        this.green = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    }
};
