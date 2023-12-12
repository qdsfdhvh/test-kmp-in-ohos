import { hello, testFlow } from '../kmp/shared';

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
