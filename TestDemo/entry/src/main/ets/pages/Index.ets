import {
  CounterEventDecrement,
  CounterEventIncrement,
  CounterPresenter,
  CounterState,
  getDeviceType1,
  getProcessPid,
  getProcessUid,
  hello
} from 'js/shared';
import process from '@ohos.process';
import deviceInfo from '@ohos.deviceInfo';

@Entry
@Component
struct Index {
  @State message: string = hello()
  @State state: CounterState = CounterState.Companion.default()

  build() {
    Row() {
      Column() {
        Text(this.message)
          .fontSize(50)
          .fontWeight(FontWeight.Bold)
        Divider()
          .height(6)
        Text(`uid=${process.uid},pid=${process.pid}`)
        Text(`uid=${getProcessUid()},pid=${getProcessPid()}`)
        Text(`device=${getDeviceType1()}`)
        Row() {
          Button('-')
            .borderRadius(8)
            .backgroundColor(0x317aff)
            .width(90)
            .height(40)
            .onClick(() => {
              this.state.event(new CounterEventDecrement());
            })
          Text(this.state.count.toString())
          Button('+')
            .borderRadius(8)
            .backgroundColor(0x317aff)
            .width(90)
            .height(40)
            .onClick(() => {
              this.state.event(new CounterEventIncrement());
            })
        }
      }
      .width('100%')
    }
    .height('100%')
    .onAppear(() => {
      new CounterPresenter().collectPresenter((state) => {
        this.state = state;
      });
    })
  }
}
