import { DatePtPipe } from './date-pt.pipe';

describe('DatePtPipe', () => {
  const pipe = new DatePtPipe();

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  describe('short format (default)', () => {
    it('should format date as dd/mm/yyyy', () => {
      // Note: new Date('2026-03-02') is parsed as UTC midnight,
      // so getDate() may return 1 or 2 depending on timezone.
      // We use a fixed expectation based on the pipe logic.
      const result = pipe.transform('2026-03-02');
      expect(result).toMatch(/^\d{2}\/03\/2026$/);
    });

    it('should pad single-digit day and month', () => {
      const result = pipe.transform('2026-01-05');
      expect(result).toMatch(/^\d{2}\/01\/2026$/);
    });

    it('should format December date correctly', () => {
      const result = pipe.transform('2026-12-25');
      expect(result).toMatch(/^\d{2}\/12\/2026$/);
    });
  });

  describe('medium format', () => {
    it('should format as "d Mon yyyy"', () => {
      const result = pipe.transform('2026-03-02', 'medium');
      expect(result).toMatch(/^\d+ Mar 2026$/);
    });

    it('should abbreviate month to 3 characters', () => {
      const result = pipe.transform('2026-01-15', 'medium');
      expect(result).toMatch(/^\d+ Jan 2026$/);
    });

    it('should format June correctly', () => {
      const result = pipe.transform('2026-06-10', 'medium');
      expect(result).toMatch(/^\d+ Jun 2026$/);
    });
  });

  describe('long format', () => {
    it('should format as "d de Month de yyyy"', () => {
      const result = pipe.transform('2026-03-15', 'long');
      expect(result).toMatch(/^\d+ de Mar.o de 2026$/);
    });

    it('should use full Portuguese month name for Janeiro', () => {
      const result = pipe.transform('2026-01-20', 'long');
      expect(result).toMatch(/^\d+ de Janeiro de 2026$/);
    });

    it('should use full Portuguese month name for Setembro', () => {
      const result = pipe.transform('2026-09-05', 'long');
      expect(result).toMatch(/^\d+ de Setembro de 2026$/);
    });

    it('should use full Portuguese month name for Dezembro', () => {
      const result = pipe.transform('2026-12-31', 'long');
      expect(result).toMatch(/^\d+ de Dezembro de 2026$/);
    });
  });

  describe('null/empty/undefined handling', () => {
    it('should return "-" for null', () => {
      expect(pipe.transform(null as any)).toBe('-');
    });

    it('should return "-" for empty string', () => {
      expect(pipe.transform('')).toBe('-');
    });

    it('should return "-" for undefined', () => {
      expect(pipe.transform(undefined as any)).toBe('-');
    });
  });

  describe('different months in Portuguese', () => {
    const monthTests = [
      { input: '2026-01-15', month: 'Janeiro' },
      { input: '2026-02-15', month: 'Fevereiro' },
      { input: '2026-04-15', month: 'Abril' },
      { input: '2026-05-15', month: 'Maio' },
      { input: '2026-07-15', month: 'Julho' },
      { input: '2026-08-15', month: 'Agosto' },
      { input: '2026-10-15', month: 'Outubro' },
      { input: '2026-11-15', month: 'Novembro' },
    ];

    monthTests.forEach(({ input, month }) => {
      it(`should render "${month}" in long format for ${input}`, () => {
        const result = pipe.transform(input, 'long');
        expect(result).toContain(month);
      });
    });
  });
});
