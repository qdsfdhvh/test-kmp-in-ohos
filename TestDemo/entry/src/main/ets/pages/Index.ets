import { getProcessUid, getProcessPid, hello, testFlow } from '../kmp/shared';
import process from '@ohos.process';

@Entry
@Component
struct Index {
  @State message: string = hello()
  @State num: number = 0

  build() {
    Row() {
      Column() {
        Text(this.message)
          .fontSize(50)
          .fontWeight(FontWeight.Bold)
        Text(this.num.toString())
        Divider()
          .height(6)
        Text(`uid=${process.uid},pid=${process.pid}`)
        Text(`uid=${getProcessUid()},pid=${getProcessPid()}`)
      }
      .width('100%')
    }
    .height('100%')
    .onAppear(() => {
      testFlow((num1) => {
        this.num = num1;
      })
    })
  }
}
