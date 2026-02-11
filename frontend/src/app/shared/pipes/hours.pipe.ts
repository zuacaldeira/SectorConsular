import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'hours', standalone: true })
export class HoursPipe implements PipeTransform {
  transform(value: number): string {
    if (!value && value !== 0) return '-';
    return `${value}h`;
  }
}
