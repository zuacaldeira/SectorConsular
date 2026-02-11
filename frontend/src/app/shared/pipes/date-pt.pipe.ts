import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'datePt', standalone: true })
export class DatePtPipe implements PipeTransform {
  private months = [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ];

  transform(value: string, format: string = 'short'): string {
    if (!value) return '-';
    const date = new Date(value);
    const day = date.getDate();
    const month = this.months[date.getMonth()];
    const year = date.getFullYear();

    if (format === 'long') return `${day} de ${month} de ${year}`;
    if (format === 'medium') return `${day} ${month.substring(0, 3)} ${year}`;
    return `${day.toString().padStart(2, '0')}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${year}`;
  }
}
